package moneytracker.repositories.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import moneytracker.model.Filter;
import moneytracker.model.Tag;
import moneytracker.model.Transaction;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.TransactionRepository;
import moneytracker.views.TransactionIndexView;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.metrics.NumericMetricsAggregation;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final Logger LOG = Logger.getLogger(TransactionRepositoryImpl.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private static final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
        .serializationInclusion(JsonInclude.Include.NON_NULL)
        .build();

    private static final ObjectReader reader = mapper.readerFor(Transaction.class).withView(TransactionIndexView.class);
    private static final ObjectWriter writer = mapper.writerWithView(TransactionIndexView.class);

    @Autowired
    private Client elastic;

    @Override
    public Transaction get(ApplicationUser owner, String id) {
        SearchResponse response = elastic.prepareSearch("transactions")
            .setTypes("transaction")
            .setFetchSource(
                new String[] {},
                new String[] {"owner"}
            )
            .setQuery(
                QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("owner", owner.getId()))
                    .must(QueryBuilders.termQuery("_id", id))
            )
            .setFrom(0)
            .setSize(1)
            .get();

        SearchHit hit = response.getHits().getAt(0);

        try {
            Transaction transaction = reader.readValue(hit.getSourceAsString());
            transaction.setId(hit.getId());
            transaction.setOwner(owner);

            return transaction;
        } catch (IOException e) {
            LOG.error(e);
            return null;
        }
    }

    @Override
    public void insert(List<Transaction> transactions) {
        transactions.forEach(transaction -> {
            try {
                elastic.prepareIndex("transactions", "transaction")
                    .setId(transaction.getId())
                    .setSource(writer.writeValueAsString(transaction))
                    .get();
            } catch (IOException e) {
                LOG.error(e);
            }
        });
    }

    @Override
    // TODO: reimplement this method to do a bulk update instead of making multiple api calls
    public void update(List<Transaction> transactions) {
        transactions.forEach(this::update);
    }

    @Override
    public void update(Transaction transaction) {
        try {
            elastic.prepareUpdate("transactions", "transaction", transaction.getId())
                .setDoc(writer.writeValueAsString(transaction))
                .setRefresh(true)
                .get();
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    @Override
    public boolean idExists(String id) {
        SearchResponse response = elastic.prepareSearch("transactions")
            .setTypes("transaction")
            .setQuery(QueryBuilders.termQuery("_id", id))
            .setFrom(0)
            .setSize(1)
            .get();

        return response.getHits().totalHits() == 1;
    }

    public List<Transaction> list(ApplicationUser owner) {
        return getList(owner, QueryBuilders.termQuery("owner", owner.getId()));
    }

    public List<Transaction> list(ApplicationUser owner, Filter filter) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("owner", owner.getId()));

        filterByDate(filter, queryBuilder);
        filterByDescription(filter, queryBuilder);
        filterByAmount(filter, queryBuilder);
        filterByAccountHolder(filter, queryBuilder);
        filterByAccount(filter, queryBuilder);
        filterByOffsetAccount(filter, queryBuilder);
        filterByTags(filter, queryBuilder);
        filterByOptions(filter, queryBuilder);

        return getList(owner, queryBuilder);
    }

    private void filterByDate(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byDateFrom() || filter.byDateTo()) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("date");

            if (filter.byDateFrom()) {
                rangeQuery.gte(formatter.format(filter.getFrom()));
            }

            if (filter.byDateTo()) {
                rangeQuery.lt(formatter.format(filter.getTo()));
            }

            queryBuilder.must(rangeQuery);
        }
    }

    private void filterByDescription(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byDescription()) {
            queryBuilder.must(
                QueryBuilders.matchQuery("description", filter.getDescription())
                    .operator(MatchQueryBuilder.Operator.AND)
            );
        }
    }

    private void filterByAmount(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byAmountFrom() || filter.byAmountTo()) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("amount");

            if (filter.byAmountFrom()) {
                rangeQuery.gte(filter.getAmount().getFrom());
            }

            if (filter.byAmountTo()) {
                rangeQuery.lte(filter.getAmount().getTo());
            }

            queryBuilder.must(rangeQuery);
        }
    }

    private void filterByAccountHolder(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byAccountHolder()) {
            queryBuilder.must(
                QueryBuilders.matchQuery("accountHolder", filter.getAccountHolder())
                    .operator(MatchQueryBuilder.Operator.AND)
            );
        }
    }

    private void filterByAccount(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byAccount()) {
            queryBuilder.must(
                QueryBuilders.matchQuery("account", filter.getAccount())
                    .operator(MatchQueryBuilder.Operator.AND)
            );
        }
    }

    private void filterByOffsetAccount(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byOffsetAccount()) {
            queryBuilder.must(
                QueryBuilders.matchQuery("offsetAccount", filter.getOffsetAccount())
                    .operator(MatchQueryBuilder.Operator.AND)
            );
        }
    }

    private void filterByTags(Filter filter, BoolQueryBuilder queryBuilder) {
        if (filter.byTags()) {
            queryBuilder.must(
                QueryBuilders.nestedQuery(
                    "tags",
                    QueryBuilders.termsQuery(
                        "tags.id",
                        filter.getTags().stream().map(Tag::getId).collect(Collectors.toList())
                    )
                )
            );
        }
    }

    private void filterByOptions(Filter filter, BoolQueryBuilder queryBuilder) {
        if (!filter.byOptions()) {
            return;
        }

        if (filter.byArchived()) {
            queryBuilder.must(QueryBuilders.termQuery("archived", true));
        }

        if (filter.byLocked()) {
            queryBuilder.must(QueryBuilders.termQuery("locked", true));
        }

        if (filter.byUntagged() && !filter.byTags()) {
            queryBuilder.mustNot(QueryBuilders.nestedQuery(
                "tags",
                QueryBuilders.existsQuery("tags.id")
            ));
        }
    }

    @Override
    public void removeFromTransactions(Tag tag) {
        List<Transaction> transactions = list(tag.getOwner(), tag);
        transactions.forEach(transaction -> transaction.getTags().remove(tag));
        update(transactions);
    }

    @Override
    public Map<Long, BigDecimal> pieChart(ApplicationUser owner, Filter filter) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("owner", owner.getId()));

        filterByDate(filter, queryBuilder);
        filterByDescription(filter, queryBuilder);
        filterByAmount(filter, queryBuilder);
        filterByAccountHolder(filter, queryBuilder);
        filterByAccount(filter, queryBuilder);
        filterByOffsetAccount(filter, queryBuilder);
        filterByTags(filter, queryBuilder);
        filterByOptions(filter, queryBuilder);

        SearchResponse response = elastic.prepareSearch("transactions")
            .setTypes("transaction")
            .setQuery(queryBuilder)
            .addAggregation(AggregationBuilders.nested("by_tag")
                .path("tags")
                .subAggregation(AggregationBuilders.terms("by_id")
                    .field("tags.id")
                    .subAggregation(AggregationBuilders.reverseNested("reverse_nested")
                        .subAggregation(AggregationBuilders.sum("sum_amount")
                            .field("amount")
                        )
                    )
                )
            )
            .setFrom(0)
            .setSize(0)
            .get();

        SingleBucketAggregation byTag = response.getAggregations().get("by_tag");
        MultiBucketsAggregation byId = byTag.getAggregations().get("by_id");
        List<? extends MultiBucketsAggregation.Bucket> buckets = byId.getBuckets();

        Map<Long, BigDecimal> result = new HashMap<>();
        buckets.forEach(bucket -> {
            SingleBucketAggregation reverseNested = bucket.getAggregations().get("reverse_nested");
            NumericMetricsAggregation.SingleValue sumAmount = reverseNested.getAggregations().get("sum_amount");
            result.put((Long) bucket.getKey(), new BigDecimal(sumAmount.getValueAsString()));
        });

        return result;
    }

    private List<Transaction> list(ApplicationUser owner, Tag tag) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
            .must(QueryBuilders.termQuery(
                "owner",
                owner.getId())
            )
            .must(QueryBuilders.nestedQuery(
                "tags",
                QueryBuilders.termQuery("tags.id", tag.getId()))
            );

        return getList(owner, queryBuilder);
    }

    private List<Transaction> getList(ApplicationUser owner, QueryBuilder queryBuilder) {
        SearchRequestBuilder requestBuilder = elastic.prepareSearch("transactions")
            .setTypes("transaction")
            .setFetchSource(
                new String[] {},
                new String[] {"owner"}
            )
            .setQuery(queryBuilder)
            .addSort("date", SortOrder.DESC)
            .setFrom(0)
            .setSize(10000);

        String query = requestBuilder.toString();

        SearchResponse response = requestBuilder.get();

        List<Transaction> transactions = new ArrayList<>();

        response.getHits().forEach(hit -> {
            try {
                Transaction transaction = reader.readValue(hit.getSourceAsString());
                transaction.setId(hit.getId());
                transaction.setOwner(owner);

                transactions.add(transaction);
            } catch (IOException e) {
                LOG.error(e);
            }
        });

        return transactions;
    }

}

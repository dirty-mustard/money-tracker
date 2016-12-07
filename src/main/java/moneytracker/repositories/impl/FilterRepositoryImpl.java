package moneytracker.repositories.impl;

import moneytracker.model.Filter;
import moneytracker.model.FilterOption;
import moneytracker.model.Tag;
import moneytracker.model.User;
import moneytracker.repositories.FilterRepository;
import moneytracker.repositories.mappers.FilterMapper;
import moneytracker.repositories.mappers.FilterOptionMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Repository
public class FilterRepositoryImpl implements FilterRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    @Autowired
    public FilterRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
            .withTableName("MT_TB_FILTERS")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Filter get(User owner, Long id) {
        Filter filter = template.queryForObject(
            "SELECT ID, CREATED_AT, NAME, AMOUNT_FROM, AMOUNT_TO, DESCRIPTION, FROM_DATE, TO_DATE, ACCOUNT_HOLDER, " +
                    "ACCOUNT, OFFSET_ACCOUNT " +
                "FROM MT_TB_FILTERS " +
                "WHERE OWNER_ID = :ownerId AND ID = :id",
            new MapSqlParameterSource()
                .addValue("ownerId", owner.getId())
                .addValue("id", id),
            new FilterMapper(owner)
        );

        List<FilterOption> options = template.query(
            "SELECT NAME " +
                "FROM MT_TB_FILTER_OPTIONS " +
                "WHERE FILTER_ID = :filterId",
            new MapSqlParameterSource("filterId", id),
            new FilterOptionMapper()
        );

        filter.setOptions(options);

        return filter;
    }

    @Override
    public void save(Filter filter) {
        if (filter.isNew()) {
            insert(filter);
        } else {
            update(filter);
        }
    }

    private void insert(Filter filter) {
        Number id = insert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("CREATED_AT", filter.getCreatedAt());
            put("NAME", filter.getName());
            put("AMOUNT_FROM", filter.byAmountFrom() ? filter.getAmount().getFrom() : null);
            put("AMOUNT_TO", filter.byAmountTo() ? filter.getAmount().getTo() : null);
            put("DESCRIPTION", filter.getDescription());
            put("FROM_DATE", filter.getFrom());
            put("TO_DATE", filter.getTo());
            put("ACCOUNT_HOLDER", filter.getAccountHolder());
            put("ACCOUNT", filter.getAccount());
            put("OFFSET_ACCOUNT", filter.getOffsetAccount());
            put("OWNER_ID", filter.getOwner().getId());
        }});
        filter.setId(id.longValue());

        List<Tag> tags = filter.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> template.update(
                "INSERT INTO MT_TB_FILTER_TAGS (FILTER_ID, TAG_ID) " +
                    "VALUES (:filterId, :tagId)",
                new MapSqlParameterSource()
                    .addValue("filterId", filter.getId())
                    .addValue("tagId", tag.getId())
            ));
        }

        List<FilterOption> filterOptions = filter.getOptions();
        if (CollectionUtils.isNotEmpty(filterOptions)) {
            filterOptions.forEach(option -> template.update(
                "INSERT INTO MT_TB_FILTER_OPTIONS (FILTER_ID, NAME) " +
                    "VALUES (:filterId, :name)",
                new MapSqlParameterSource()
                    .addValue("filterId", filter.getId())
                    .addValue("name", option.toString())
            ));
        }
    }

    private void update(Filter filter) {
        template.update(
            "UPDATE MT_TB_FILTERS " +
                "SET NAME = :name, AMOUNT_FROM = :amountFrom, AMOUNT_TO = :amountTo, DESCRIPTION = :description, " +
                    "FROM_DATE = :fromDate, TO_DATE = :toDate, ACCOUNT_HOLDER = :accountHolder, ACCOUNT = :account, " +
                    "OFFSET_ACCOUNT = :offsetAccount " +
                "WHERE ID = :id",
            new MapSqlParameterSource()
                .addValue("name", filter.getName())
                .addValue("amountFrom", filter.byAmountFrom() ? filter.getAmount().getFrom() : null)
                .addValue("amountTo", filter.byAmountTo() ? filter.getAmount().getTo() : null)
                .addValue("description", filter.getDescription())
                .addValue("fromDate", filter.getFrom())
                .addValue("toDate", filter.getTo())
                .addValue("accountHolder", filter.getAccountHolder())
                .addValue("account", filter.getAccount())
                .addValue("offsetAccount", filter.getOffsetAccount())
                .addValue("id", filter.getId())
        );

        template.update(
            "DELETE FROM MT_TB_FILTER_TAGS " +
                "WHERE FILTER_ID = :filterId",
            new MapSqlParameterSource("filterId", filter.getId())
        );

        List<Tag> tags = filter.getTags();
        if (CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> template.update(
                "INSERT INTO MT_TB_FILTER_TAGS (FILTER_ID, TAG_ID) " +
                    "VALUES (:filterId, :tagId)",
                new MapSqlParameterSource()
                    .addValue("filterId", filter.getId())
                    .addValue("tagId", tag.getId())
            ));
        }

        template.update(
            "DELETE FROM MT_TB_FILTER_OPTIONS " +
                "WHERE FILTER_ID = :filterId",
            new MapSqlParameterSource("filterId", filter.getId())
        );

        List<FilterOption> options = filter.getOptions();
        if (CollectionUtils.isNotEmpty(options)) {
            options.forEach(option -> template.update(
                "INSERT INTO MT_TB_FILTER_OPTIONS (FILTER_ID, NAME) " +
                    "VALUES (:filterId, :name)",
                new MapSqlParameterSource()
                    .addValue("filterId", filter.getId())
                    .addValue("name", option.name())
            ));
        }
    }

    @Override
    public void remove(Filter filter) {
        template.update(
            "DELETE FROM MT_TB_FILTER_TAGS " +
                "WHERE FILTER_ID = :filterId",
            new MapSqlParameterSource("filterId", filter.getId())
        );

        template.update(
            "DELETE FROM MT_TB_FILTER_OPTIONS " +
                "WHERE FILTER_ID = :filterId",
            new MapSqlParameterSource("filterId", filter.getId())
        );

        template.update(
            "DELETE FROM MT_TB_FILTERS " +
                "WHERE ID = :id",
            new MapSqlParameterSource("id", filter.getId())
        );
    }

    @Override
    public List<Filter> list(User owner) {
        List<Filter> filters = template.query(
            "SELECT ID, CREATED_AT, NAME, AMOUNT_FROM, AMOUNT_TO, DESCRIPTION, FROM_DATE, TO_DATE, ACCOUNT_HOLDER, " +
                    "ACCOUNT, OFFSET_ACCOUNT " +
                "FROM MT_TB_FILTERS " +
                "WHERE OWNER_ID = :ownerId " +
                "ORDER BY ID ASC",
            new MapSqlParameterSource("ownerId", owner.getId()),
            new FilterMapper(owner)
        );

        filters.forEach(filter -> {
            List<FilterOption> options = template.query(
                "SELECT NAME " +
                    "FROM MT_TB_FILTER_OPTIONS " +
                    "WHERE FILTER_ID = :filterId",
                new MapSqlParameterSource("filterId", filter.getId()),
                new FilterOptionMapper()
            );

            filter.setOptions(options);
        });

        return filters;
    }

}

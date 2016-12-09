package moneytracker.repositories.impl;

import moneytracker.model.Rule;
import moneytracker.model.Tag;
import moneytracker.model.User;
import moneytracker.repositories.RuleRepository;
import moneytracker.repositories.mappers.RuleMapper;
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
public class RuleRepositoryImpl implements RuleRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    @Autowired
    public RuleRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
            .withTableName("MT_TB_RULES")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Rule get(User owner, Long id) {
        return template.queryForObject(
            "SELECT ID, CREATED_AT, ARCHIVE, ENABLED, FILTER_ID " +
                "FROM MT_TB_RULES " +
                "WHERE OWNER_ID = :ownerId AND ID = :id",
            new MapSqlParameterSource()
                .addValue("ownerId", owner.getId())
                .addValue("id", id),
            new RuleMapper(owner)
        );
    }

    @Override
    public void save(Rule rule) {
        if (rule.isNew()) {
            insert(rule);
        } else {
            update(rule);
        }
    }

    private void insert(Rule rule) {
        Number id = insert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("CREATED_AT", rule.getCreatedAt());
            put("ARCHIVE", rule.isArchive());
            put("ENABLED", rule.isEnabled());
            put("FILTER_ID", rule.getFilter().getId());
            put("OWNER_ID", rule.getOwner().getId());
        }});

        rule.setId(id.longValue());

        List<Tag> tags = rule.getTagsToApply();
        if (CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> template.update(
                "INSERT INTO MT_TB_RULE_TAGS (RULE_ID, TAG_ID) " +
                    "VALUES (:ruleId, :tagId)",
                new MapSqlParameterSource()
                    .addValue("ruleId", rule.getId())
                    .addValue("tagId", tag.getId())
            ));
        }
    }

    private void update(Rule rule) {
        template.update(
            "UPDATE MT_TB_RULES " +
                "SET ARCHIVE = :archive, ENABLED = :enabled, FILTER_ID = :filterId " +
                "WHERE ID = :id",
            new MapSqlParameterSource()
                .addValue("archive", rule.isArchive())
                .addValue("enabled", rule.isEnabled())
                .addValue("filterId", rule.getFilter().getId())
                .addValue("id", rule.getId())
        );

        template.update(
            "DELETE FROM MT_TB_RULE_TAGS " +
                "WHERE RULE_ID = :ruleId",
            new MapSqlParameterSource("ruleId", rule.getId())
        );

        List<Tag> tags = rule.getTagsToApply();
        if (CollectionUtils.isNotEmpty(tags)) {
            tags.forEach(tag -> template.update(
                "INSERT INTO MT_TB_RULE_TAGS (RULE_ID, TAG_ID) " +
                    "VALUES (:ruleId, :tagId)",
                new MapSqlParameterSource()
                    .addValue("ruleId", rule.getId())
                    .addValue("tagId", tag.getId())
            ));
        }
    }

    @Override
    public void remove(Rule rule) {
        template.update(
            "DELETE FROM MT_TB_RULE_TAGS " +
                "WHERE RULE_ID = :ruleId",
            new MapSqlParameterSource("ruleId", rule.getId())
        );

        template.update(
            "DELETE FROM MT_TB_RULES " +
                "WHERE ID = :id",
            new MapSqlParameterSource("id", rule.getId())
        );
    }

    @Override
    public List<Rule> list(User owner) {
        return template.query(
            "SELECT ID, CREATED_AT, ARCHIVE, ENABLED, FILTER_ID " +
                "FROM MT_TB_RULES " +
                "WHERE OWNER_ID = :ownerId " +
                "ORDER BY ID ASC",
            new MapSqlParameterSource("ownerId", owner.getId()),
            new RuleMapper(owner)
        );
    }

}

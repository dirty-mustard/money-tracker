package moneytracker.repositories.impl;

import moneytracker.model.Filter;
import moneytracker.model.Rule;
import moneytracker.model.Tag;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.TagRepository;
import moneytracker.repositories.mappers.TagMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Repository
public class TagRepositoryImpl implements TagRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    @Autowired
    public TagRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
            .withTableName("MT_TB_TAGS")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Tag get(ApplicationUser owner, Long id) {
        return template.queryForObject(
            "SELECT ID, CREATED_AT, NAME, COLOR " +
                "FROM MT_TB_TAGS " +
                "WHERE OWNER_ID = :ownerId AND ID = :id",
            new MapSqlParameterSource()
                .addValue("ownerId", owner.getId())
                .addValue("id", id),
            new TagMapper(owner)
        );
    }

    @Override
    public void save(Tag tag) {
        if (tag.isNew()) {
            insert(tag);
        } else {
            update(tag);
        }
    }

    private void insert(Tag tag) {
        Number id = insert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("CREATED_AT", tag.getCreatedAt());
            put("NAME", tag.getName());
            put("COLOR", tag.getColor());
            put("OWNER_ID", tag.getOwner().getId());
        }});

        tag.setId(id.longValue());
    }

    private void update(Tag tag) {
        template.update(
            "UPDATE MT_TB_TAGS " +
                "SET NAME = :name, COLOR = :color " +
                "WHERE ID = :id",
            new MapSqlParameterSource()
                .addValue("name", tag.getName())
                .addValue("color", tag.getColor())
                .addValue("id", tag.getId())
        );
    }

    @Override
    public void remove(Tag tag) {
        template.update(
            "DELETE FROM MT_TB_TAGS " +
                "WHERE ID = :id",
            new MapSqlParameterSource("id", tag.getId())
        );
    }

    @Override
    public List<Tag> list(ApplicationUser owner) {
        return template.query(
            "SELECT ID, CREATED_AT, NAME, COLOR " +
                "FROM MT_TB_TAGS " +
                "WHERE OWNER_ID = :ownerId " +
                "ORDER BY NAME ASC",
            new MapSqlParameterSource("ownerId", owner.getId()),
            new TagMapper(owner)
        );
    }

    @Override
    public List<Tag> list(ApplicationUser owner, List<Long> ids) {
        return template.query(
            "SELECT ID, CREATED_AT, NAME, COLOR " +
                "FROM MT_TB_TAGS " +
                "WHERE OWNER_ID = :ownerId AND ID IN (:ids) " +
                "ORDER BY NAME ASC",
            new MapSqlParameterSource()
                .addValue("ownerId", owner.getId())
                .addValue("ids", ids),
            new TagMapper(owner)
        );
    }

    @Override
    public List<Tag> list(ApplicationUser owner, Filter filter) {
        return template.query(
            "SELECT T.ID, T.CREATED_AT, T.NAME, T.COLOR " +
                "FROM MT_TB_TAGS T " +
                "JOIN MT_TB_FILTER_TAGS FT " +
                    "ON FT.TAG_ID = T.ID " +
                "WHERE FT.FILTER_ID = :filterId " +
                "ORDER BY T.NAME ASC",
            new MapSqlParameterSource()
                .addValue("filterId", filter.getId()),
            new TagMapper(owner)
        );
    }

    @Override
    public List<Tag> list(ApplicationUser owner, Rule rule) {
        return template.query(
            "SELECT T.ID, T.CREATED_AT, T.NAME, T.COLOR " +
                "FROM MT_TB_TAGS T " +
                "JOIN MT_TB_RULE_TAGS RT " +
                    "ON RT.TAG_ID = T.ID " +
                "WHERE RT.RULE_ID = :ruleId " +
                "ORDER BY T.NAME ASC",
            new MapSqlParameterSource()
                .addValue("ruleId", rule.getId()),
            new TagMapper(owner)
        );
    }

}

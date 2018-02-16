package moneytracker.repositories.mappers;

import moneytracker.model.Filter;
import moneytracker.model.Rule;
import moneytracker.model.ApplicationUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RuleMapper implements RowMapper<Rule> {

    private ApplicationUser owner;

    public RuleMapper(ApplicationUser owner) {
        this.owner = owner;
    }

    @Override
    public Rule mapRow(ResultSet resultSet, int i) throws SQLException {
        Rule rule = new Rule();
        rule.setId(resultSet.getLong("ID"));
        rule.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
        rule.setEnabled(resultSet.getBoolean("ENABLED"));
        rule.setArchive(resultSet.getBoolean("ARCHIVE"));

        rule.setOwner(owner);

        Filter filter = new Filter();
        filter.setId(resultSet.getLong("FILTER_ID"));
        rule.setFilter(filter);

        return rule;
    }

}

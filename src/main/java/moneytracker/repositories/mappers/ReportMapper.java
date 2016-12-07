package moneytracker.repositories.mappers;

import moneytracker.model.Filter;
import moneytracker.model.Report;
import moneytracker.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportMapper implements RowMapper<Report> {

    private User owner;

    public ReportMapper(User owner) {
        this.owner = owner;
    }

    @Override
    public Report mapRow(ResultSet resultSet, int i) throws SQLException {
        Report report = new Report();
        report.setId(resultSet.getLong("ID"));
        report.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
        report.setName(resultSet.getString("NAME"));

        report.setOwner(owner);

        Filter filter = new Filter();
        filter.setId(resultSet.getLong("FILTER_ID"));
        report.setFilter(filter);

        return report;
    }

}

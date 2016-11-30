package moneytracker.repositories.mappers;

import moneytracker.model.ReportChart;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class ReportChartMapper implements RowMapper<ReportChart> {

    @Override
    public ReportChart mapRow(ResultSet resultSet, int i) throws SQLException {
        return ReportChart.valueOf(resultSet.getString("NAME"));
    }

}

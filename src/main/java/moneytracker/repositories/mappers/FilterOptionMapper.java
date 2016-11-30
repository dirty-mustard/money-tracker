package moneytracker.repositories.mappers;

import moneytracker.model.FilterOption;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class FilterOptionMapper implements RowMapper<FilterOption> {

    @Override
    public FilterOption mapRow(ResultSet resultSet, int i) throws SQLException {
        return FilterOption.valueOf(resultSet.getString("NAME"));
    }

}

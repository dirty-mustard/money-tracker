package moneytracker.repositories.mappers;

import moneytracker.model.ApplicationUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<ApplicationUser> {

    @Override
    public ApplicationUser mapRow(ResultSet resultSet, int i) throws SQLException {
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setId(resultSet.getLong("ID"));
        applicationUser.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
        applicationUser.setUsername(resultSet.getString("USERNAME"));
        applicationUser.setPassword(resultSet.getString("PASSWORD"));

        return applicationUser;
    }

}

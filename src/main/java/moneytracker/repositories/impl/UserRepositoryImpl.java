package moneytracker.repositories.impl;

import moneytracker.model.User;
import moneytracker.repositories.UserRepository;
import moneytracker.repositories.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public final class UserRepositoryImpl implements UserRepository {

    private NamedParameterJdbcTemplate template;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public User getByUsername(String username) {
        return template.queryForObject(
            "SELECT ID, CREATED_AT, USERNAME, PASSWORD " +
                "FROM MT_TB_USERS " +
                "WHERE USERNAME = :username",
            new MapSqlParameterSource("username", username),
            new UserMapper()
        );
    }

}

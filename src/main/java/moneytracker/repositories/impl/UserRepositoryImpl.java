package moneytracker.repositories.impl;

import moneytracker.model.ApplicationUser;
import moneytracker.repositories.UserRepository;
import moneytracker.repositories.mappers.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
                .withTableName("MT_TB_USERS")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public ApplicationUser getByUsername(String username) {
        return template.queryForObject(
            "SELECT ID, CREATED_AT, USERNAME, PASSWORD " +
                "FROM MT_TB_USERS " +
                "WHERE USERNAME = :username",
            new MapSqlParameterSource("username", username),
            new UserMapper()
        );
    }

    @Override
    public void save(ApplicationUser applicationUser) {
        Number id = insert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("USERNAME", applicationUser.getUsername());
            put("PASSWORD", applicationUser.getPassword());
            put("CREATED_AT", applicationUser.getCreatedAt());
        }});
        applicationUser.setId(id.longValue());
    }

}

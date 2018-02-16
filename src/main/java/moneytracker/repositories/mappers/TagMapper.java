package moneytracker.repositories.mappers;

import moneytracker.model.Tag;
import moneytracker.model.ApplicationUser;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagMapper implements RowMapper<Tag> {

    private ApplicationUser owner;

    public TagMapper(ApplicationUser owner) {
        this.owner = owner;
    }

    @Override
    public Tag mapRow(ResultSet resultSet, int i) throws SQLException {
        Tag tag = new Tag();
        tag.setId(resultSet.getLong("ID"));
        tag.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
        tag.setName(resultSet.getString("NAME"));
        tag.setColor(resultSet.getString("COLOR"));

        tag.setOwner(owner);

        return tag;
    }

}

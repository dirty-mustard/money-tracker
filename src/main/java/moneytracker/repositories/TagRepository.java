package moneytracker.repositories;

import moneytracker.model.Filter;
import moneytracker.model.Rule;
import moneytracker.model.Tag;
import moneytracker.model.User;

import java.util.List;

public interface TagRepository {

    Tag get(User owner, Long id);

    void save(Tag tag);

    void remove(Tag tag);

    List<Tag> list(User owner);

    List<Tag> list(User owner, List<Long> ids);

    List<Tag> list(User owner, Filter filter);

    List<Tag> list(User owner, Rule rule);

}

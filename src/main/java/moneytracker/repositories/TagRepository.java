package moneytracker.repositories;

import moneytracker.model.Filter;
import moneytracker.model.Rule;
import moneytracker.model.Tag;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface TagRepository {

    Tag get(ApplicationUser owner, Long id);

    void save(Tag tag);

    void remove(Tag tag);

    List<Tag> list(ApplicationUser owner);

    List<Tag> list(ApplicationUser owner, List<Long> ids);

    List<Tag> list(ApplicationUser owner, Filter filter);

    List<Tag> list(ApplicationUser owner, Rule rule);

}

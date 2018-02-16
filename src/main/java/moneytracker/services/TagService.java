package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Tag;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface TagService {

    List<Tag> list(ApplicationUser owner);

    List<Tag> list(ApplicationUser owner, List<Long> ids);

    Tag get(ApplicationUser owner, Long id) throws NotFoundException;

    void save(Tag tag);

    void remove(Tag tag);

}

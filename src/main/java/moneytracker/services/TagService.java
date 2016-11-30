package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.Tag;
import moneytracker.model.Transaction;
import moneytracker.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface TagService {

    List<Tag> list(User owner);

    List<Tag> list(User owner, List<Long> ids);

    Tag get(User owner, Long id) throws NotFoundException;

    void save(Tag tag);

    void remove(Tag tag);

}

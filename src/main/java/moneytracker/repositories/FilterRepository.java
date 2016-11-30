package moneytracker.repositories;

import moneytracker.model.Filter;
import moneytracker.model.User;

import java.util.List;

public interface FilterRepository {

    Filter get(User owner, Long id);

    void save(Filter filter);

    void remove(Filter filter);

    List<Filter> list(User owner);

}

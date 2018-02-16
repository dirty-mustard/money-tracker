package moneytracker.repositories;

import moneytracker.model.Filter;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface FilterRepository {

    Filter get(ApplicationUser owner, Long id);

    void save(Filter filter);

    void remove(Filter filter);

    List<Filter> list(ApplicationUser owner);

}

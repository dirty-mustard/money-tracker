package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface FilterService {

    List<Filter> list(ApplicationUser owner);

    Filter get(ApplicationUser owner, Long id) throws NotFoundException;

    void save(Filter filter);

    void remove(Filter filter);

}

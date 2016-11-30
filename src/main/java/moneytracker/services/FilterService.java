package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.User;

import java.util.List;

public interface FilterService {

    List<Filter> list(User owner);

    Filter get(User owner, Long id) throws NotFoundException;

    void save(Filter filter);

    void remove(Filter filter);

}

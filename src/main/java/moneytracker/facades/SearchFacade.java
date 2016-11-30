package moneytracker.facades;

import moneytracker.model.Filter;
import moneytracker.model.SearchResult;
import moneytracker.model.User;

public interface SearchFacade {

    SearchResult search(User owner, Filter filter);

}

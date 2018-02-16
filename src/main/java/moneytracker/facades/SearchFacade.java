package moneytracker.facades;

import moneytracker.model.Filter;
import moneytracker.model.SearchResult;
import moneytracker.model.ApplicationUser;

public interface SearchFacade {

    SearchResult search(ApplicationUser owner, Filter filter);

}

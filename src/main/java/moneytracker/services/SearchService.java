package moneytracker.services;

import moneytracker.model.SearchResult;
import moneytracker.model.ApplicationUser;
import moneytracker.model.Filter;

public interface SearchService {

    SearchResult search(ApplicationUser owner, Filter filter);

}

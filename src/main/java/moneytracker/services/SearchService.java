package moneytracker.services;

import moneytracker.model.SearchResult;
import moneytracker.model.User;
import moneytracker.model.Filter;

public interface SearchService {

    SearchResult search(User owner, Filter filter);

}

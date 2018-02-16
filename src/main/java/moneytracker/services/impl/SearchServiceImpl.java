package moneytracker.services.impl;

import moneytracker.model.SearchResult;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.TransactionRepository;
import moneytracker.model.Filter;
import moneytracker.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public SearchResult search(ApplicationUser owner, Filter filter) {
        SearchResult searchResult = new SearchResult();
        searchResult.setTransactions(transactionRepository.list(owner, filter));

        return searchResult;
    }

}

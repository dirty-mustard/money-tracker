package moneytracker.facades.impl;

import moneytracker.facades.SearchFacade;
import moneytracker.model.Filter;
import moneytracker.model.SearchResult;
import moneytracker.model.User;
import moneytracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchFacadeImpl implements SearchFacade {

    @Autowired
    private TransactionService transactionService;

    public SearchResult search(User owner, Filter filter) {
        SearchResult result = new SearchResult();
        result.setTransactions(transactionService.search(owner, filter));

        return result;
    }

}

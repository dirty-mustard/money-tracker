package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.PieChart;
import moneytracker.model.Transaction;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface TransactionService {

    List<Transaction> search(ApplicationUser owner, Filter filter);

    Transaction get(ApplicationUser owner, String id) throws NotFoundException;

    void update(Transaction transaction);

    void archive(Transaction transaction);

    void unarchive(Transaction transaction);

    void lock(Transaction transaction);

    void unlock(Transaction transaction);

    PieChart pieChart(ApplicationUser owner, Filter filter);

}

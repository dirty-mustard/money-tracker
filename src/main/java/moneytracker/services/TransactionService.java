package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.PieChart;
import moneytracker.model.Transaction;
import moneytracker.model.User;

import java.util.List;

public interface TransactionService {

    List<Transaction> search(User owner, Filter filter);

    Transaction get(User owner, String id) throws NotFoundException;

    void update(Transaction transaction);

    void archive(Transaction transaction);

    void unarchive(Transaction transaction);

    void lock(Transaction transaction);

    void unlock(Transaction transaction);

    PieChart pieChart(User owner, Filter filter);

}

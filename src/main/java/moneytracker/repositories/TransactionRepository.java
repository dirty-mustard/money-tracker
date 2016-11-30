package moneytracker.repositories;

import moneytracker.model.Filter;
import moneytracker.model.Tag;
import moneytracker.model.Transaction;
import moneytracker.model.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransactionRepository {

    Transaction get(User owner, String id);

    void insert(List<Transaction> transactions);

    void update(List<Transaction> transactions);

    void update(Transaction transaction);

    boolean idExists(String id);

    List<Transaction> list(User owner);

    List<Transaction> list(User owner, Filter filter);

    void removeFromTransactions(Tag tag);

    Map<Long, BigDecimal> pieChart(User owner, Filter filter);

}

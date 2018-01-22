package moneytracker.services.impl;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.PieChart;
import moneytracker.model.Tag;
import moneytracker.model.Transaction;
import moneytracker.model.User;
import moneytracker.repositories.TagRepository;
import moneytracker.repositories.TransactionRepository;
import moneytracker.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Transaction> search(User owner, Filter filter) {
        List<Transaction> transactions = transactionRepository.list(owner, filter);
        transactions.forEach(
            transaction -> transaction.setTags(
                tagRepository.list(
                    owner,
                    transaction.getTags().stream().map(Tag::getId).collect(Collectors.toList())
                )
            )
        );

        return transactions;
    }

    @Override
    public Transaction get(User owner, String id) throws NotFoundException {
        try {
            return transactionRepository.get(owner, id);
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("Transaction [id:%s] not found", id));
        }
    }

    @Override
    public void update(Transaction transaction) {
        lock(transaction);
    }

    @Override
    public void archive(Transaction transaction) {
        transaction.setArchived(true);
        transactionRepository.update(transaction);
    }

    @Override
    public void unarchive(Transaction transaction) {
        transaction.setArchived(false);
        transactionRepository.update(transaction);
    }

    @Override
    public void lock(Transaction transaction) {
        transaction.setLocked(true);
        transactionRepository.update(transaction);
    }

    @Override
    public void unlock(Transaction transaction) {
        transaction.setLocked(false);
        transactionRepository.update(transaction);
    }

    @Override
    public PieChart pieChart(User owner, Filter filter) {
        PieChart chart = new PieChart();

        transactionRepository.pieChart(owner, filter).entrySet().forEach(entry -> {
            Tag tag = tagRepository.get(owner, entry.getKey());
            chart.getPoints().add(new PieChart.Point(tag.getName(), entry.getValue(), tag.getColor()));
        });

        return chart;
    }

}

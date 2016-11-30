package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Rule;
import moneytracker.model.Transaction;
import moneytracker.model.User;

import java.util.List;

public interface RuleService {

    List<Rule> list(User owner);

    Rule get(User owner, Long id) throws NotFoundException;

    void save(Rule rule);

    void remove(Rule rule);

    void runAll(User owner);

    void runAll(User owner, Transaction transaction);

}

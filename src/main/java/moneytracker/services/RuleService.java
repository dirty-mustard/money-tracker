package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Rule;
import moneytracker.model.Transaction;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface RuleService {

    List<Rule> list(ApplicationUser owner);

    Rule get(ApplicationUser owner, Long id) throws NotFoundException;

    void save(Rule rule);

    void remove(Rule rule);

    void runAll(ApplicationUser owner);

    void runAll(ApplicationUser owner, Transaction transaction);

}

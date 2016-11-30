package moneytracker.repositories;

import moneytracker.model.Rule;
import moneytracker.model.User;

import java.util.List;

public interface RuleRepository {

    Rule get(User owner, Long id);

    void save(Rule rule);

    void remove(Rule rule);

    List<Rule> list(User owner);

}

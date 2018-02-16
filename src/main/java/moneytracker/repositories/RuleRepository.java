package moneytracker.repositories;

import moneytracker.model.Rule;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface RuleRepository {

    Rule get(ApplicationUser owner, Long id);

    void save(Rule rule);

    void remove(Rule rule);

    List<Rule> list(ApplicationUser owner);

}

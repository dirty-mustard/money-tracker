package moneytracker.services.impl;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Rule;
import moneytracker.model.Transaction;
import moneytracker.model.User;
import moneytracker.repositories.FilterRepository;
import moneytracker.repositories.RuleRepository;
import moneytracker.repositories.TagRepository;
import moneytracker.repositories.TransactionRepository;
import moneytracker.services.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Rule> list(User owner) {
        List<Rule> rules = ruleRepository.list(owner);
        rules.forEach(rule -> {
            rule.setTagsToApply(tagRepository.list(owner, rule));
            rule.setFilter(filterRepository.get(owner, rule.getFilter().getId()));
        });

        return rules;
    }

    @Override
    public Rule get(User owner, Long id) throws NotFoundException {
        try {
            Rule rule = ruleRepository.get(owner, id);
            rule.setTagsToApply(tagRepository.list(owner, rule));
            rule.setFilter(filterRepository.get(owner, rule.getFilter().getId()));

            return rule;
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("Rule [id:%d] not found", id));
        }
    }

    @Override
    @Transactional
    public void save(Rule rule) {
        ruleRepository.save(rule);
    }

    @Override
    @Transactional
    public void remove(Rule rule) {
        ruleRepository.remove(rule);
    }

    @Override
    @Transactional
    public void runAll(User owner) {
        List<Transaction> transactions = transactionRepository.list(owner);
        transactions.stream().filter(transaction -> !transaction.isLocked()).forEach(transaction -> {
            transaction.getTags().clear();
            runAll(owner, transaction);
        });

        transactionRepository.update(transactions);
    }

    @Override
    public void runAll(User owner, Transaction transaction) {
        List<Rule> rules = list(owner);
        rules.stream().filter(
            rule -> !transaction.isLocked() && rule.isEnabled() && rule.getFilter().matches(transaction)
        ).forEach(rule -> {
            transaction.getTags().addAll(rule.getTagsToApply());

            // FIXME: if a rule is configured to archive the matched transaction and the next rule isn't,
            // FIXME: this transaction ends up not being archived, the next rule always overwrites
            // FIXME: the previous one
            // TODO: maybe we shouldn't archive transactions via rules
            transaction.setArchived(rule.isArchive());
        });

    }

}

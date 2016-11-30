package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.Filter;
import moneytracker.model.Transaction;
import moneytracker.model.User;
import moneytracker.security.SecurityContext;
import moneytracker.services.TagService;
import moneytracker.services.TransactionService;
import moneytracker.views.TransactionView;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionsController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/_search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(TransactionView.class)
    public List<Transaction> search(@RequestBody Filter filter) {
        return transactionService.search(securityContext.getAuthenticatedUser(), filter);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(TransactionView.class)
    public Transaction get(@PathVariable String id) {
        return transactionService.get(securityContext.getAuthenticatedUser(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(TransactionView.class)
    public Transaction update(@PathVariable String id, @RequestBody List<Long> tagsIds) {
        User owner = securityContext.getAuthenticatedUser();

        Transaction transaction = transactionService.get(owner, id);
        if (CollectionUtils.isNotEmpty(tagsIds)) {
            transaction.setTags(tagService.list(owner, tagsIds));
        }
        transactionService.update(transaction);

        return transaction;
    }

    @RequestMapping(value = "/{id}/archive", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void archive(@PathVariable String id) {
        Transaction transaction = transactionService.get(securityContext.getAuthenticatedUser(), id);
        transactionService.archive(transaction);
    }

    @RequestMapping(value = "/{id}/unarchive", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unarchive(@PathVariable String id) {
        Transaction transaction = transactionService.get(securityContext.getAuthenticatedUser(), id);
        transactionService.unarchive(transaction);
    }

    @RequestMapping(value = "/{id}/lock", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void lock(@PathVariable String id) {
        Transaction transaction = transactionService.get(securityContext.getAuthenticatedUser(), id);
        transactionService.lock(transaction);
    }

    @RequestMapping(value = "/{id}/unlock", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlock(@PathVariable String id) {
        Transaction transaction = transactionService.get(securityContext.getAuthenticatedUser(), id);
        transactionService.unlock(transaction);
    }

}

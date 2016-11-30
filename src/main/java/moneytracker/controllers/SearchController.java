package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.facades.SearchFacade;
import moneytracker.model.Filter;
import moneytracker.model.SearchResult;
import moneytracker.security.SecurityContext;
import moneytracker.views.TransactionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/_search")
public class SearchController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private SearchFacade searchFacade;

    @RequestMapping(method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(TransactionView.class)
    public SearchResult search(@RequestBody Filter filter) {
        return searchFacade.search(securityContext.getAuthenticatedUser(), filter);
    }

}

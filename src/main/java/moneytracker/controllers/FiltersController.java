package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.security.SecurityContext;
import moneytracker.model.Filter;
import moneytracker.services.FilterService;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.FilterView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/filters")
public class FiltersController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private FilterService filterService;

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(FilterView.class)
    public List<Filter> list() {
        return filterService.list(securityContext.getAuthenticatedUser());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(FilterView.class)
    public Filter create(@RequestBody @Validated(Adding.class) Filter filter) {
        Filter targetFilter = new Filter();
        BeanUtils.copyProperties(filter, targetFilter, "id", "createdAt", "owner");

        targetFilter.setOwner(securityContext.getAuthenticatedUser());
        filterService.save(targetFilter);

        return targetFilter;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(FilterView.class)
    public Filter get(@PathVariable Long id) {
        return filterService.get(securityContext.getAuthenticatedUser(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(FilterView.class)
    public Filter update(@PathVariable Long id, @RequestBody @Validated(Updating.class) Filter filter) {
        Filter targetFilter = filterService.get(securityContext.getAuthenticatedUser(), id);

        BeanUtils.copyProperties(filter, targetFilter, "id", "createdAt", "owner");
        filterService.save(targetFilter);

        return targetFilter;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        Filter filter = filterService.get(securityContext.getAuthenticatedUser(), id);
        filterService.remove(filter);
    }

}

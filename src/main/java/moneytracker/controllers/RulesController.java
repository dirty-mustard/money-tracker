package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.Rule;
import moneytracker.security.SecurityContext;
import moneytracker.services.RuleService;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.RuleView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rules")
public class RulesController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private RuleService ruleService;

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(RuleView.class)
    public List<Rule> list() {
        return ruleService.list(securityContext.getAuthenticatedUser());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(RuleView.class)
    public Rule create(@RequestBody @Validated(Adding.class) Rule rule) {
        Rule targetRule = new Rule();
        BeanUtils.copyProperties(rule, targetRule, "id", "createdAt", "owner");

        targetRule.setOwner(securityContext.getAuthenticatedUser());
        ruleService.save(targetRule);

        return targetRule;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(RuleView.class)
    public Rule get(@PathVariable Long id) {
        return ruleService.get(securityContext.getAuthenticatedUser(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(RuleView.class)
    public Rule update(@PathVariable Long id, @RequestBody @Validated(Updating.class) Rule rule) {
        Rule targetRule = ruleService.get(securityContext.getAuthenticatedUser(), id);

        BeanUtils.copyProperties(rule, targetRule, "id", "createdAt", "owner");
        ruleService.save(targetRule);

        return targetRule;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        Rule rule = ruleService.get(securityContext.getAuthenticatedUser(), id);
        ruleService.remove(rule);
    }

    @RequestMapping(value = "/_runAll", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void runAll() {
        ruleService.runAll(securityContext.getAuthenticatedUser());
    }

}

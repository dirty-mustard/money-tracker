package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.Tag;
import moneytracker.security.SecurityContext;
import moneytracker.services.TagService;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.TagView;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagsController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private TagService tagService;

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(TagView.class)
    public List<Tag> list(@RequestParam(value = "ids", required = false) List<Long> ids) {
        return (CollectionUtils.isNotEmpty(ids))
            ? tagService.list(securityContext.getAuthenticatedUser(), ids)
            : tagService.list(securityContext.getAuthenticatedUser());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(TagView.class)
    public Tag create(@RequestBody @Validated(Adding.class) Tag tag) {
        Tag targetTag = new Tag();
        BeanUtils.copyProperties(tag, targetTag, "id", "createdAt", "owner");

        targetTag.setOwner(securityContext.getAuthenticatedUser());
        tagService.save(targetTag);

        return targetTag;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(TagView.class)
    public Tag get(@PathVariable Long id) {
        return tagService.get(securityContext.getAuthenticatedUser(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(TagView.class)
    public Tag update(@PathVariable Long id, @RequestBody @Validated(Updating.class) Tag tag) {
        Tag targetTag = tagService.get(securityContext.getAuthenticatedUser(), id);

        BeanUtils.copyProperties(tag, targetTag, "id", "createdAt", "owner");
        tagService.save(targetTag);

        return targetTag;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        Tag tag = tagService.get(securityContext.getAuthenticatedUser(), id);
        tagService.remove(tag);
    }

}

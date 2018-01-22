package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import moneytracker.deserializers.FilterDeserializer;
import moneytracker.deserializers.TagDeserializer;
import moneytracker.validation.Adding;
import moneytracker.views.RuleView;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Rule implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(RuleView.class)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonView(RuleView.class)
    private Date createdAt = new Date();

    // Validation
    @NotNull(groups = Adding.class, message = "A filter must be specified")
    // Serialization
    @JsonView(RuleView.class)
    @JsonDeserialize(using = FilterDeserializer.class)
    private Filter filter;

    @JsonView(RuleView.class)
    private boolean enabled = true;

    @JsonView(RuleView.class)
    private boolean archive = false;

    @JsonView(RuleView.class)
    @JsonDeserialize(contentUsing = TagDeserializer.class)
    private List<Tag> tagsToApply = new ArrayList<>();

    private User owner;

    @Override
    @JsonView
    public boolean isNew() {
        return (id == null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public List<Tag> getTagsToApply() {
        return tagsToApply;
    }

    public void setTagsToApply(List<Tag> tagsToApply) {
        this.tagsToApply = tagsToApply;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Rule rule = (Rule) o;

        return Objects.equals(id, rule.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

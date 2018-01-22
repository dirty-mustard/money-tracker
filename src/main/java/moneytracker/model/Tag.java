package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.RuleView;
import moneytracker.views.TagView;
import moneytracker.views.TransactionIndexView;
import moneytracker.views.TransactionView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tag implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView({
        TagView.class,
        TransactionView.class,
        TransactionIndexView.class,
        RuleView.class
    })
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonView(TagView.class)
    private Date createdAt = new Date();

    // Validation
    @NotNull(groups = Adding.class, message = "A name must be specified")
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 100)
    // Serialization
    @JsonView({TagView.class, TransactionView.class, RuleView.class})
    private String name;

    // Validation
    @NotNull(groups = Adding.class, message = "A color must be specified")
    @Size(groups = {Adding.class, Updating.class}, min = 7, max = 7)
    // Serialization
    @JsonView({TagView.class, TransactionView.class})
    private String color;

    @JsonView
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

        Tag tag = (Tag) o;

        return Objects.equals(id, tag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name;
    }

}

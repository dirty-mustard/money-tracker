package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ApplicationUser implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Date createdAt = new Date();

    @NotNull(groups = Adding.class)
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 50)
    private String username;

    @NotNull(groups = Adding.class)
    @Size(groups = {Adding.class, Updating.class}, min = 8, max = 60)
    private String password;

    @Override
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicationUser applicationUser = (ApplicationUser) o;

        return Objects.equals(id, applicationUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import moneytracker.deserializers.DateDeserializer;
import moneytracker.views.TransactionIndexView;
import moneytracker.views.TransactionView;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transaction implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    private static final int AMOUNT_SCALE = 2;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @JsonView(TransactionView.class)
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @JsonView({TransactionView.class, TransactionIndexView.class})
    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private BigDecimal amount;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private String name;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private String description;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private String accountHolder;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private String account;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private String offsetAccount;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private List<Tag> tags = new ArrayList<>();

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private boolean locked = false;

    @JsonView({TransactionView.class, TransactionIndexView.class})
    private boolean archived = false;

    @JsonView(TransactionIndexView.class)
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @Override
    @JsonView
    public boolean isNew() {
        return (id == null);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOffsetAccount() {
        return offsetAccount;
    }

    public void setOffsetAccount(String offsetAccount) {
        this.offsetAccount = offsetAccount;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void generateId() {
        StringBuilder builder = new StringBuilder();

        if (date != null) {
            builder.append(formatter.format(date));
        }

        if (StringUtils.isNotBlank(name)) {
            builder.append(name);
        }

        if (StringUtils.isNotBlank(accountHolder)) {
            builder.append(accountHolder);
        }

        if (StringUtils.isNotBlank(offsetAccount)) {
            builder.append(offsetAccount);
        }

        if (amount != null) {
            builder.append(amount.setScale(AMOUNT_SCALE, BigDecimal.ROUND_HALF_EVEN));
        }

        id = UUID.nameUUIDFromBytes(builder.toString().getBytes()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Transaction transaction = (Transaction) o;

        return Objects.equals(id, transaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

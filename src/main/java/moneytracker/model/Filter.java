package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import moneytracker.deserializers.DateDeserializer;
import moneytracker.deserializers.TagDeserializer;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.FilterView;
import moneytracker.views.RuleView;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Filter implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView({FilterView.class, RuleView.class})
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonView(FilterView.class)
    private Date createdAt = new Date();

    // Validation
    @NotNull(groups = Adding.class, message = "A name must be specified")
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 100)
    // Serialization
    @JsonView({FilterView.class, RuleView.class})
    private String name;

    @JsonView(FilterView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date from;

    @JsonView(FilterView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date to;

    // Validation
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 200)
    // Serialization
    @JsonView(FilterView.class)
    private String description;

    @JsonView(FilterView.class)
    private AmountFilter amount;

    // Validation
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 50)
    // Serialization
    @JsonView(FilterView.class)
    private String accountHolder;

    // Validation
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 50)
    // Serialization
    @JsonView(FilterView.class)
    private String account;

    // Validation
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 50)
    // Serialization
    @JsonView(FilterView.class)
    private String offsetAccount;

    @JsonView(FilterView.class)
    @JsonDeserialize(contentUsing = TagDeserializer.class)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Tag> tags = new ArrayList<>();

    @JsonView(FilterView.class)
    private List<FilterOption> options = new ArrayList<>();

    private ApplicationUser owner;

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

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public boolean byDateFrom() {
        return from != null;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public boolean byDateTo() {
        return to != null;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean byDescription() {
        return StringUtils.isNotEmpty(description);
    }

    public AmountFilter getAmount() {
        return amount;
    }

    public void setAmount(AmountFilter amount) {
        this.amount = amount;
    }

    public boolean byAmountFrom() {
        return amount != null && amount.byFrom();
    }

    public boolean byAmountTo() {
        return amount != null && amount.byTo();
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public boolean byAccountHolder() {
        return StringUtils.isNotEmpty(accountHolder);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean byAccount() {
        return StringUtils.isNotEmpty(account);
    }

    public String getOffsetAccount() {
        return offsetAccount;
    }

    public void setOffsetAccount(String offsetAccount) {
        this.offsetAccount = offsetAccount;
    }

    public boolean byOffsetAccount() {
        return StringUtils.isNotEmpty(offsetAccount);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean byTags() {
        return CollectionUtils.isNotEmpty(tags);
    }

    public List<FilterOption> getOptions() {
        return options;
    }

    public void setOptions(List<FilterOption> options) {
        this.options = options;
    }

    public boolean byOptions() {
        return CollectionUtils.isNotEmpty(options);
    }

    public boolean byArchived() {
        return IterableUtils.contains(options, FilterOption.ARCHIVED);
    }

    public boolean byLocked() {
        return IterableUtils.contains(options, FilterOption.LOCKED);
    }

    public boolean byUntagged() {
        return IterableUtils.contains(options, FilterOption.UNTAGGED);
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
        this.owner = owner;
    }

    private boolean isEmpty() {
        return !byDateFrom() && !byDateTo() && !byDescription() && !byAmountFrom() && !byAmountTo() &&
               !byAccountHolder() && !byAccount() && !byOffsetAccount();
    }

    public boolean matches(Transaction transaction) {
        return !(isEmpty() ||
                (byDateFrom() && !transaction.getDate().after(from)) ||
                (byDateTo() && !transaction.getDate().before(to)) ||
                (byDescription() && !StringUtils.containsIgnoreCase(transaction.getDescription(), description)) ||
                (byAmountFrom() && transaction.getAmount().compareTo(amount.getFrom()) < 0) ||
                (byAmountTo() && transaction.getAmount().compareTo(amount.getTo()) > 0) ||
                (byAccountHolder() && !StringUtils.containsIgnoreCase(transaction.getAccountHolder(), accountHolder)) ||
                (byAccount() && !StringUtils.containsIgnoreCase(transaction.getAccount(), account)) ||
                (byOffsetAccount() && !StringUtils.containsIgnoreCase(transaction.getOffsetAccount(), offsetAccount)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Filter filter = (Filter) o;

        return Objects.equals(id, filter.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

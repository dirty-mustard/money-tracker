package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import moneytracker.deserializers.FilterDeserializer;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.ReportView;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Report implements Entity, Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(ReportView.class)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @JsonView(ReportView.class)
    private Date createdAt = new Date();

    // Validation
    @NotNull(groups = Adding.class, message = "A name must be specified")
    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 100)
    // Serialization
    @JsonView(ReportView.class)
    private String name;

    @Size(groups = {Adding.class, Updating.class}, min = 1, max = 100)
    // Serialization
    @JsonView(ReportView.class)
    private String icon;

    // Validation
    @NotNull(groups = Adding.class, message = "A filter must be specified")
    // Serialization
    @JsonView(ReportView.class)
    @JsonDeserialize(using = FilterDeserializer.class)
    @JsonIdentityReference(alwaysAsId = true)
    private Filter filter;

    @JsonView(ReportView.class)
    private List<ReportChart> charts = new ArrayList<ReportChart>() {{
        add(ReportChart.LINE);
        add(ReportChart.PIE);
    }};

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<ReportChart> getCharts() {
        return charts;
    }

    public void setCharts(List<ReportChart> charts) {
        this.charts = charts;
    }

    public ApplicationUser getOwner() {
        return owner;
    }

    public void setOwner(ApplicationUser owner) {
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

        Report report = (Report) o;

        return Objects.equals(id, report.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

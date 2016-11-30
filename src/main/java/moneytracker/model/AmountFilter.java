package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.views.FilterView;

import java.math.BigDecimal;

public class AmountFilter {

    @JsonView(FilterView.class)
    private BigDecimal from;

    @JsonView(FilterView.class)
    private BigDecimal to;

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public boolean byFrom() {
        return from != null;
    }

    public boolean byTo() {
        return to != null;
    }

}

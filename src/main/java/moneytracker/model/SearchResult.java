package moneytracker.model;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.views.TransactionView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchResult implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonView(TransactionView.class)
    private List<Transaction> transactions = new ArrayList<>();

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}

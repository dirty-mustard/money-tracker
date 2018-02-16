package moneytracker.repositories.mappers;

import moneytracker.model.AmountFilter;
import moneytracker.model.Filter;
import moneytracker.model.ApplicationUser;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterMapper implements RowMapper<Filter> {

    private ApplicationUser owner;

    public FilterMapper(ApplicationUser owner) {
        this.owner = owner;
    }

    @Override
    public Filter mapRow(ResultSet resultSet, int i) throws SQLException {
        Filter filter = new Filter();
        filter.setId(resultSet.getLong("ID"));
        filter.setName(resultSet.getString("NAME"));
        filter.setCreatedAt(resultSet.getTimestamp("CREATED_AT"));
        filter.setFrom(resultSet.getDate("FROM_DATE"));
        filter.setTo(resultSet.getDate("TO_DATE"));
        filter.setDescription(resultSet.getString("DESCRIPTION"));
        filter.setAccountHolder(resultSet.getString("ACCOUNT_HOLDER"));
        filter.setAccount(resultSet.getString("ACCOUNT"));
        filter.setOffsetAccount(resultSet.getString("OFFSET_ACCOUNT"));

        filter.setOwner(owner);

        BigDecimal amountFrom = resultSet.getBigDecimal("AMOUNT_FROM");
        BigDecimal amountTo = resultSet.getBigDecimal("AMOUNT_TO");

        if (amountFrom != null || amountTo != null) {
            AmountFilter amountFilter = new AmountFilter();
            amountFilter.setFrom(amountFrom);
            amountFilter.setTo(amountTo);
            filter.setAmount(amountFilter);
        }

        return filter;
    }

}

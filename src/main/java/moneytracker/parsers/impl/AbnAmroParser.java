package moneytracker.parsers.impl;

import moneytracker.parsers.TransactionParserException;
import moneytracker.model.Transaction;
import moneytracker.parsers.AbstractTransactionParser;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AbnAmroParser extends AbstractTransactionParser {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private DecimalFormat amountFormatter = (DecimalFormat) DecimalFormat.getInstance(new Locale("nl"));

    public AbnAmroParser() {
        super(
            '\t',
            new String[] {
                "account", "currency", "date1", "balanceBefore", "balanceAfter", "date2", "amount", "description"
            },
            false
        );
    }

    @Override
    public Transaction parse(CSVRecord record) throws TransactionParserException {
        try {
            Transaction transaction = new Transaction();

            transaction.setAccount(record.get("account"));

            Date date = dateFormatter.parse(record.get("date1"));
            transaction.setDate(date);

            amountFormatter.setParseBigDecimal(true);
            BigDecimal amount = (BigDecimal) amountFormatter.parseObject(record.get("amount"));
            transaction.setAmount(amount);

            transaction.setDescription(record.get("description"));

            return transaction;
        } catch (ParseException e) {
            throw new TransactionParserException("Unable to parse transaction", e);
        }
    }

}

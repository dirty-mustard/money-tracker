package moneytracker.parsers.impl;

import moneytracker.parsers.TransactionParserException;
import moneytracker.model.Transaction;
import moneytracker.parsers.AbstractTransactionParser;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class IngParser extends AbstractTransactionParser {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private DecimalFormat amountFormatter = (DecimalFormat) DecimalFormat.getInstance(new Locale("nl"));

    public IngParser() {
        super(
            new String[] {
                "transactionDate", "accountHolder", "accountNumber", "offsetAccount", "code", "transactionType",
                "amount", "mutationcode", "description"
            },
            true
        );
    }

    @Override
    public Transaction parse(CSVRecord record) throws TransactionParserException {
        try {
            Transaction transaction = new Transaction();

            String dateAsString = record.get("transactionDate");
            transaction.setDate(dateFormatter.parse(dateAsString));

            amountFormatter.setParseBigDecimal(true);
            StringBuilder amountBuilder = new StringBuilder();
            if (record.get("transactionType").equals("Af")) {
                amountBuilder.append("-");
            }
            amountBuilder.append(record.get("amount"));
            BigDecimal amount = (BigDecimal) amountFormatter.parseObject(amountBuilder.toString());
            transaction.setAmount(amount);

            transaction.setName(record.get("accountHolder"));
            transaction.setDescription(
                String.format("%s %s", record.get("accountHolder"), record.get("description"))
            );

            transaction.setAccountHolder(record.get("accountHolder"));
            transaction.setAccount(record.get("accountNumber"));
            transaction.setOffsetAccount(record.get("offsetAccount"));

            return transaction;
        } catch (ParseException e) {
            throw new TransactionParserException("Unable to parse transaction", e);
        }
    }

}

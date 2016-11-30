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

public final class RabobankParser extends AbstractTransactionParser {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
    private DecimalFormat amountFormatter = (DecimalFormat) DecimalFormat.getInstance(new Locale("nl"));

    public RabobankParser() {
        super(
            new String[] {
                "accountNumber", "currency", "interestDate", "transactionType", "amount", "offsetAccount",
                "accountHolder", "transactionDate", "code", "filler", "description1", "description2", "description3",
                "description4", "description5", "description6", "endToEndId", "offsetAccountId", "mandate"
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

            StringBuilder amountBuilder = new StringBuilder();
            if (record.get("transactionType").equals("D")) {
                amountBuilder.append("-");
            }
            amountBuilder.append(record.get("amount"));
            BigDecimal amount = (BigDecimal) amountFormatter.parseObject(amountBuilder.toString());
            transaction.setAmount(amount);

            transaction.setAccountHolder(record.get("accountHolder"));
            transaction.setAccount(record.get("accountNumber"));
            transaction.setOffsetAccount(record.get("offsetAccount"));

            transaction.setName(record.get("accountHolder"));

            String description = String.format("%s %s %s %s %s %s %s", record.get("accountHolder"),
                record.get("description1"), record.get("description2"), record.get("description3"),
                record.get("description4"), record.get("description5"), record.get("description6"));
            transaction.setDescription(description);

            return transaction;
        } catch (ParseException e) {
            throw new TransactionParserException("Unable to parse transaction", e);
        }
    }

}

package moneytracker.parsers;

import moneytracker.model.Transaction;
import org.apache.commons.csv.CSVRecord;

public interface TransactionParser {

    char delimiter();

    String[] csvHeaders();

    boolean skipHeaderRecord();

    Transaction parse(CSVRecord record) throws TransactionParserException;

}

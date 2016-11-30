package moneytracker.parsers;

public abstract class AbstractTransactionParser implements TransactionParser {

    private char delimiter;

    private String[] csvHeaders;

    private boolean skipHeaderRecord;

    public AbstractTransactionParser(String[] csvHeaders, boolean skipHeaderRecord) {
        this(',', csvHeaders, skipHeaderRecord);
    }

    public AbstractTransactionParser(char delimiter, String[] csvHeaders, boolean skipHeaderRecord) {
        this.delimiter = delimiter;
        this.csvHeaders = csvHeaders;
        this.skipHeaderRecord = skipHeaderRecord;
    }

    @Override
    public char delimiter() {
        return this.delimiter;
    }

    @Override
    public String[] csvHeaders() {
        return csvHeaders;
    }

    @Override
    public boolean skipHeaderRecord() {
        return skipHeaderRecord;
    }

}

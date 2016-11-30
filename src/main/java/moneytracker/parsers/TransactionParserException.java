package moneytracker.parsers;

import moneytracker.exceptions.ApplicationException;

public class TransactionParserException extends ApplicationException {

    public TransactionParserException() {
        // Empty constructor
    }

    public TransactionParserException(String message) {
        super(message);
    }

    public TransactionParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionParserException(Throwable cause) {
        super(cause);
    }

    public TransactionParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

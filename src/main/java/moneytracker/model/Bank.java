package moneytracker.model;

import moneytracker.exceptions.UnexpectedException;
import moneytracker.parsers.TransactionParser;
import moneytracker.parsers.impl.AbnAmroParser;
import moneytracker.parsers.impl.IngParser;
import moneytracker.parsers.impl.RabobankParser;

public enum Bank {

    ABN_AMRO ("ABN AMRO", AbnAmroParser.class),
    ING      ("ING",      IngParser.class),
    RABOBANK ("Rabobank", RabobankParser.class);

    private String description;

    private Class<? extends TransactionParser> parserClass;

    Bank(String description, Class<? extends TransactionParser> parserClass) {
        this.description = description;
        this.parserClass = parserClass;
    }

    public String getDescription() {
        return description;
    }

    public TransactionParser getParser() {
        try {
            return parserClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnexpectedException("Something went really wrong", e);
        }
    }

}

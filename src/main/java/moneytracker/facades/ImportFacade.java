package moneytracker.facades;

import moneytracker.model.Bank;
import moneytracker.model.User;
import moneytracker.parsers.TransactionParserException;

import java.io.InputStream;

public interface ImportFacade {

    void importCsv(User owner, Bank bank, InputStream inputStream) throws TransactionParserException;

}

package moneytracker.facades;

import moneytracker.model.Bank;
import moneytracker.model.ApplicationUser;
import moneytracker.parsers.TransactionParserException;

import java.io.InputStream;

public interface ImportFacade {

    void importCsv(ApplicationUser owner, Bank bank, InputStream inputStream) throws TransactionParserException;

}

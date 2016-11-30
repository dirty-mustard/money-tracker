package moneytracker.facades.impl;

import moneytracker.facades.ImportFacade;
import moneytracker.model.Bank;
import moneytracker.model.Transaction;
import moneytracker.model.User;
import moneytracker.parsers.TransactionParser;
import moneytracker.parsers.TransactionParserException;
import moneytracker.repositories.TransactionRepository;
import moneytracker.services.RuleService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

@Service
public final class ImportFacadeImpl implements ImportFacade {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RuleService ruleService;

    public void importCsv(User owner, Bank bank, InputStream inputStream) throws TransactionParserException {
        try {

            InputStreamReader reader = new InputStreamReader(inputStream);
            TransactionParser parser = bank.getParser();

            CSVFormat format = CSVFormat.DEFAULT
                    .withDelimiter(parser.delimiter())
                    .withHeader(parser.csvHeaders())
                    .withSkipHeaderRecord(parser.skipHeaderRecord());

            Iterable<CSVRecord> records = format.parse(reader);
            List<Transaction> transactions = new LinkedList<>();

            records.forEach(record -> {
                Transaction transaction = parser.parse(record);
                transaction.setOwner(owner);
                transaction.generateId();

                if (!transactionRepository.idExists(transaction.getId())) {
                    ruleService.runAll(owner, transaction);
                    transactions.add(transaction);
                }
            });

            reader.close();
            inputStream.close();

            transactionRepository.insert(transactions);

        } catch (IOException e) {
            throw new TransactionParserException("Unable to parse transactions", e);
        }
    }

}

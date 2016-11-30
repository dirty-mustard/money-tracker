package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Report;
import moneytracker.model.User;

import java.util.List;

public interface ReportService {

    List<Report> list(User owner);

    Report get(User owner, Long id) throws NotFoundException;

    void save(Report report);

    void remove(Report report);

}

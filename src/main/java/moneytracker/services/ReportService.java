package moneytracker.services;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Report;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface ReportService {

    List<Report> list(ApplicationUser owner);

    Report get(ApplicationUser owner, Long id) throws NotFoundException;

    void save(Report report);

    void remove(Report report);

}

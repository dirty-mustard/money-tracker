package moneytracker.repositories;

import moneytracker.model.Report;
import moneytracker.model.ApplicationUser;

import java.util.List;

public interface ReportRepository {

    Report get(ApplicationUser owner, Long id);

    void save(Report report);

    void remove(Report report);

    List<Report> list(ApplicationUser owner);

}

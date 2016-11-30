package moneytracker.repositories;

import moneytracker.model.Report;
import moneytracker.model.User;

import java.util.List;

public interface ReportRepository {

    Report get(User owner, Long id);

    void save(Report report);

    void remove(Report report);

    List<Report> list(User owner);

}

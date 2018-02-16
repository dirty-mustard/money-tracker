package moneytracker.services.impl;

import moneytracker.exceptions.NotFoundException;
import moneytracker.model.Filter;
import moneytracker.model.Report;
import moneytracker.model.ApplicationUser;
import moneytracker.repositories.FilterRepository;
import moneytracker.repositories.ReportRepository;
import moneytracker.repositories.TagRepository;
import moneytracker.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Report> list(ApplicationUser owner) {
        List<Report> reports = reportRepository.list(owner);
        reports.forEach(report -> {
            report.setFilter(filterRepository.get(owner, report.getFilter().getId()));
        });

        return reports;
    }

    @Override
    public Report get(ApplicationUser owner, Long id) throws NotFoundException {
        try {
            Report report = reportRepository.get(owner, id);

            Filter filter = filterRepository.get(owner, report.getFilter().getId());
            filter.setTags(tagRepository.list(owner, filter));
            report.setFilter(filter);

            return report;
        } catch (DataAccessException e) {
            throw new NotFoundException(String.format("Report [id:%d] not found", id));
        }
    }

    @Override
    @Transactional
    public void save(Report report) {
        reportRepository.save(report);
    }

    @Override
    @Transactional
    public void remove(Report report) {
        reportRepository.remove(report);
    }

}

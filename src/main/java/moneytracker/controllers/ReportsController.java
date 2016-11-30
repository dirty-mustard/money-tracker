package moneytracker.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import moneytracker.model.PieChart;
import moneytracker.model.Report;
import moneytracker.model.Transaction;
import moneytracker.model.User;
import moneytracker.security.SecurityContext;
import moneytracker.services.ReportService;
import moneytracker.services.TransactionService;
import moneytracker.validation.Adding;
import moneytracker.validation.Updating;
import moneytracker.views.ReportView;
import moneytracker.views.TransactionView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    @Autowired
    private SecurityContext securityContext;

    @Autowired
    private ReportService reportService;

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    @JsonView(ReportView.class)
    public List<Report> list() {
        return reportService.list(securityContext.getAuthenticatedUser());
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @JsonView(ReportView.class)
    public Report create(@RequestBody @Validated(Adding.class) Report report) {
        Report targetReport = new Report();
        BeanUtils.copyProperties(report, targetReport, "id", "createdAt", "owner");

        targetReport.setOwner(securityContext.getAuthenticatedUser());
        reportService.save(targetReport);

        return targetReport;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @JsonView(ReportView.class)
    public Report get(@PathVariable Long id) {
        return reportService.get(securityContext.getAuthenticatedUser(), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(ReportView.class)
    public Report update(@PathVariable Long id, @RequestBody @Validated(Updating.class) Report report) {
        Report targetReport = reportService.get(securityContext.getAuthenticatedUser(), id);

        BeanUtils.copyProperties(report, targetReport, "id", "createdAt", "owner");
        reportService.save(targetReport);

        return targetReport;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remove(@PathVariable Long id) {
        Report report = reportService.get(securityContext.getAuthenticatedUser(), id);
        reportService.remove(report);
    }

    @RequestMapping(value = "/{id}/transactions", method = RequestMethod.GET)
    @JsonView(TransactionView.class)
    public List<Transaction> transactions(@PathVariable Long id) {
        User owner = securityContext.getAuthenticatedUser();
        Report report = reportService.get(owner, id);

        return transactionService.search(owner, report.getFilter());
    }

    @RequestMapping(value = "/{id}/pieChart", method = RequestMethod.GET)
    public Collection<PieChart.Point> pieChart(@PathVariable Long id) {
        User owner = securityContext.getAuthenticatedUser();
        Report report = reportService.get(owner, id);

        return transactionService.pieChart(owner, report.getFilter()).getPoints();
    }

}

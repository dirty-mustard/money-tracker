package moneytracker.repositories.impl;

import moneytracker.model.Report;
import moneytracker.model.ReportChart;
import moneytracker.model.User;
import moneytracker.repositories.ReportRepository;
import moneytracker.repositories.mappers.ReportChartMapper;
import moneytracker.repositories.mappers.ReportMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    private NamedParameterJdbcTemplate template;
    private SimpleJdbcInsert insert;

    @Autowired
    public ReportRepositoryImpl(DataSource dataSource) {
        template = new NamedParameterJdbcTemplate(dataSource);
        insert = new SimpleJdbcInsert(dataSource)
            .withTableName("MT_TB_REPORTS")
            .usingGeneratedKeyColumns("id");
    }

    @Override
    public Report get(User owner, Long id) {
        Report report = template.queryForObject(
            "SELECT ID, CREATED_AT, NAME, FILTER_ID " +
                "FROM MT_TB_REPORTS " +
                "WHERE OWNER_ID = :ownerId AND ID = :id",
            new MapSqlParameterSource()
                .addValue("ownerId", owner.getId())
                .addValue("id", id),
            new ReportMapper(owner)
        );

        List<ReportChart> charts = template.query(
            "SELECT NAME " +
                "FROM MT_TB_REPORT_CHARTS " +
                "WHERE REPORT_ID = :reportId",
            new MapSqlParameterSource("reportId", id),
            new ReportChartMapper()
        );

        report.setCharts(charts);

        return report;
    }

    @Override
    public void save(Report report) {
        if (report.isNew()) {
            insert(report);
        } else {
            update(report);
        }
    }

    private void insert(Report report) {
        Number id = insert.executeAndReturnKey(new HashMap<String, Object>() {{
            put("CREATED_AT", report.getCreatedAt());
            put("NAME", report.getName());
            put("FILTER_ID", report.getFilter().getId());
            put("OWNER_ID", report.getOwner().getId());
        }});
        report.setId(id.longValue());

        List<ReportChart> charts = report.getCharts();
        if (CollectionUtils.isNotEmpty(charts)) {
            charts.forEach(chart -> template.update(
                "INSERT INTO MT_TB_REPORT_CHARTS (REPORT_ID, NAME) " +
                    "VALUES (:reportId, :name)",
                new MapSqlParameterSource()
                    .addValue("reportId", report.getId())
                    .addValue("name", chart.toString())
            ));
        }
    }

    private void update(Report report) {
        template.update(
            "UPDATE MT_TB_REPORTS " +
                "SET NAME = :name, FILTER_ID = :filterId " +
                "WHERE ID = :id",
            new MapSqlParameterSource()
                .addValue("name", report.getName())
                .addValue("filterId", report.getFilter().getId())
                .addValue("id", report.getId())
        );

        template.update(
            "DELETE FROM MT_TB_REPORT_CHARTS " +
                "WHERE REPORT_ID = :reportId",
            new MapSqlParameterSource("reportId", report.getId())
        );

        List<ReportChart> reportCharts = report.getCharts();
        if (CollectionUtils.isNotEmpty(reportCharts)) {
            reportCharts.forEach(option -> template.update(
                "INSERT INTO MT_TB_REPORT_CHARTS (REPORT_ID, NAME) " +
                    "VALUES (:reportId, :name)",
                new MapSqlParameterSource()
                    .addValue("reportId", report.getId())
                    .addValue("name", option.name())
            ));
        }
    }

    @Override
    public void remove(Report report) {
        template.update(
            "DELETE FROM MT_TB_REPORT_CHARTS " +
                "WHERE REPORT_ID = :reportId",
            new MapSqlParameterSource("reportId", report.getId())
        );

        template.update(
            "DELETE FROM MT_TB_REPORTS " +
                "WHERE ID = :id",
            new MapSqlParameterSource("id", report.getId())
        );
    }

    @Override
    public List<Report> list(User owner) {
        List<Report> reports = template.query(
            "SELECT ID, CREATED_AT, NAME, FILTER_ID " +
                "FROM MT_TB_REPORTS " +
                "WHERE OWNER_ID = :ownerId " +
                "ORDER BY NAME ASC",
            new MapSqlParameterSource("ownerId", owner.getId()),
            new ReportMapper(owner)
        );

        reports.forEach(report -> {
            List<ReportChart> charts = template.query(
                "SELECT NAME " +
                    "FROM MT_TB_REPORT_CHARTS " +
                    "WHERE REPORT_ID = :reportId",
                new MapSqlParameterSource("reportId", report.getId()),
                new ReportChartMapper()
            );

            report.setCharts(charts);
        });

        return reports;
    }

}

package io.kontakt.apps.jdbc;

import io.kontak.apps.Anomalies;
import io.kontak.apps.Filter;
import io.kontak.apps.Pagination;
import io.kontak.apps.usecase.FindAnomalies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
class FindAnomaliesJdbc implements FindAnomalies {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FindAnomaliesJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Anomalies all() {
        return new AnomaliesJdbc(jdbcTemplate, Pagination.firstPage(), Filter.noCriteria());
    }
}

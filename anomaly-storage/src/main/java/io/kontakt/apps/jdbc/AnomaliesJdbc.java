package io.kontakt.apps.jdbc;

import io.kontak.apps.Anomalies;
import io.kontak.apps.AnomalyEntity;
import io.kontak.apps.Filter;
import io.kontak.apps.Pagination;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Stream;

class AnomaliesJdbc implements Anomalies {

    private final JdbcTemplate jdbcTemplate;
    private Pagination pagination;
    private Filter filter;

    public AnomaliesJdbc(JdbcTemplate jdbcTemplate, Pagination pagination, Filter filter) {
        this.jdbcTemplate = jdbcTemplate;
        this.pagination = pagination;
        this.filter = filter;
    }

    @Override
    public Anomalies filterBy(Filter filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public Anomalies pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    @Override
    public Stream<AnomalyEntity> stream() {
        var params = new ArrayList<>();
        var query = "SELECT room_id,thermometer_id,temperature, anomaly_time FROM anomalies";

        if (filter.roomId() != null) {
            params.add(filter.roomId());
            query = query.concat(" WHERE room_id = ?");
        }

        if (filter.thermometerId() != null) {
            params.add(filter.thermometerId());
            query = query.contains("WHERE") ? query.concat(" AND thermometer_id = ?")
                    : query.concat(" WHERE thermometer_id = ?");
        }

        if (filter.threshold() != null) {
            params.add(filter.threshold());
            query = query.contains("WHERE") ? query.concat(" AND temperature > ? GROUP BY thermometer_id")
                    : query.concat(" WHERE temperature > ? GROUP BY thermometer_id");
        }

        params.add(pagination.offset());
        params.add(pagination.limit());

        return jdbcTemplate.queryForList(
                        query.concat(" ORDER BY 1 LIMIT ?,?"),
                        params.toArray())
                .stream()
                .map(this::toDomain);
    }

    private AnomalyEntity toDomain(Map<String, Object> entry) {
        var timestamp = (Timestamp) entry.get("anomaly_time");
        return new AnomalyEntity(
                entry.get("room_id").toString(),
                entry.get("thermometer_id").toString(),
                (BigDecimal) entry.get("temperature"),
                timestamp.toInstant()
        );
    }
}
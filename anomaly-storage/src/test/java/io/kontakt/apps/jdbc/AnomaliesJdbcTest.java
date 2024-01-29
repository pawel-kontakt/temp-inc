package io.kontakt.apps.jdbc;

import io.kontak.apps.Filter;
import io.kontak.apps.Pagination;
import io.kontak.apps.usecase.FindAnomalies;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@ContextConfiguration(classes = AnomaliesJdbcConfig.class)
@Sql(statements = "INSERT INTO anomalies VALUES " +
        "(1, 'room_1', 'thermometer_id_1', 20.0, CURRENT_TIMESTAMP), " +
        "(2, 'room_2', 'thermometer_id_2', 21.0, CURRENT_TIMESTAMP), " +
        "(3, 'room_3', 'thermometer_id_3', 25.0, CURRENT_TIMESTAMP)")
class AnomaliesJdbcTest {

    @Autowired
    private FindAnomalies findAnomalies;

    @Test
    void anomalies_are_streamed() {
        var anomalies = findAnomalies.all();
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(3),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_1"),
                () -> assertThat(all.get(1).roomId()).isEqualTo("room_2"),
                () -> assertThat(all.get(2).roomId()).isEqualTo("room_3")
        );
    }

    @Test
    void anomalies_are_filtered_by_room_id() {
        var anomalies = findAnomalies.all().filterBy(Filter.builder().roomId("room_1").build());
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_1")
        );
    }

    @Test
    void anomalies_are_filtered_by_thermometer_id() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().thermometerId("thermometer_id_2").build());
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_2")
        );
    }

    @Test
    void anomalies_are_filtered_by_threshold() {
        var anomalies = findAnomalies.all().filterBy(Filter.builder().threshold(BigDecimal.valueOf(21.0)).build());
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_3")
        );
    }

    @Test
    void anomalies_are_filtered_by_room_id_and_thermometer_id() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().roomId("room_1").thermometerId("thermometer_id_1").build());
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_1")
        );
    }

    @Test
    void anomalies_are_paginated() {
        var anomalies = findAnomalies.all().pagination(new Pagination(1, 1));
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_2")
        );
    }

    @Test
    void anomalies_are_paginated_and_filtered_by_room_id() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().roomId("room_1").build())
                .pagination(new Pagination(1, 0));
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(1),
                () -> assertThat(all.get(0).roomId()).isEqualTo("room_1")
        );
    }

    @Test
    void anomalies_by_not_existing_room_not_found() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().roomId("room_4").build())
                .pagination(new Pagination(1, 1));
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(0)
        );
    }

    @Test
    void anomalies_by_not_existing_thermometer_id_not_found() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().thermometerId("thermometer_4").build())
                .pagination(new Pagination(1, 1));
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(0)
        );
    }

    @Test
    void anomalies_by_not_existing_room_id_and_thermometer_id_not_found() {
        var anomalies = findAnomalies.all()
                .filterBy(Filter.builder().roomId("room_4").thermometerId("thermometer_4").build())
                .pagination(new Pagination(1, 1));
        var all = anomalies.stream().toList();

        assertAll(
                () -> assertThat(all.size()).isEqualTo(0)
        );
    }
}
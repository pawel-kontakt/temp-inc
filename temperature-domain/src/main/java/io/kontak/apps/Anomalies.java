package io.kontak.apps;

import java.util.stream.Stream;

/**
 * Anomalies collection
 */
public interface Anomalies {

    Anomalies filterBy(Filter filter);

    Anomalies pagination(Pagination pagination);

    Stream<AnomalyEntity> stream();

}

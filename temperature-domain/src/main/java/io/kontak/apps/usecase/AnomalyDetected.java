package io.kontak.apps.usecase;

import io.kontak.apps.AnomalyEntity;

/**
 * Anomaly detected use case
 */
public interface AnomalyDetected {

    void anomalyDetected(AnomalyEntity anomaly);

}

package io.kontakt.apps.listener;

import io.kontak.apps.AnomalyEntity;
import io.kontak.apps.PlaceId;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.usecase.AnomalyDetected;
import org.apache.kafka.streams.kstream.KStream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class AnomaliesListener implements Function<KStream<String, Anomaly>, KStream<String, List<PlaceId>>> {

    final AnomalyDetected anomalyDetected;

    public AnomaliesListener(AnomalyDetected anomalyDetected) {
        this.anomalyDetected = anomalyDetected;
    }

    @Override
    public KStream<String, List<PlaceId>> apply(KStream<String, Anomaly> events) {
        return events
                .mapValues((anomaly) -> persist(List.of(anomaly)))
                .mapValues((s, placeIds) -> placeIds);
    }

    private List<PlaceId> persist(List<Anomaly> anomalyList) {
        List<PlaceId> anomalyPlaceIds = new ArrayList<>();
        anomalyList.forEach(
                anomaly -> {
                    anomalyDetected.anomalyDetected(
                            new AnomalyEntity(anomaly.roomId(), anomaly.thermometerId(), BigDecimal.valueOf(anomaly.temperature()), anomaly.timestamp()));
                    anomalyPlaceIds.add(new PlaceId(anomaly.roomId(), anomaly.thermometerId()));
                }
        );
        return anomalyPlaceIds;
    }
}

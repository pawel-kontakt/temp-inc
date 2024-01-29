package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.ArrayList;
import java.util.List;

class AnomalyDetectorTransformer implements
        Transformer<String, TemperatureReading, KeyValue<String, List<TemperatureReading>>> {

    private final Integer windowSize;
    private final String stateStoreName;
    private ProcessorContext context;
    private KeyValueStore<String, List<TemperatureReading>> store;

    public AnomalyDetectorTransformer(Integer windowSize, String stateStoreName) {
        this.windowSize = windowSize;
        this.stateStoreName = stateStoreName;
    }

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
        this.store = context.getStateStore(stateStoreName);
    }

    @Override
    public KeyValue<String, List<TemperatureReading>> transform(String key,
                                                                TemperatureReading value) {

        List<TemperatureReading> temperatureReadings = store.get(key);

        if (temperatureReadings != null) {
            temperatureReadings.add(value);
            store.put(key, temperatureReadings);
        } else {
            store.put(key, new ArrayList<>(List.of(value)));
        }

        findAndFlushCandidates();

        return null;
    }

    private void findAndFlushCandidates() {
        KeyValueIterator<String, List<TemperatureReading>> it = store.all();
        while (it.hasNext()) {
            KeyValue<String, List<TemperatureReading>> entry = it.next();
            if (entry.value.size() >= windowSize) {
                this.context.forward(entry.key, entry.value);
                store.delete(entry.key);
            }
        }
        it.close();
    }

    @Override
    public void close() {

    }
}
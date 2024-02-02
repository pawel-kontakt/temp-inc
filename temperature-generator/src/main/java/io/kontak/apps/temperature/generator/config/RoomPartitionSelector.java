package io.kontak.apps.temperature.generator.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Slf4j
@Component
public class RoomPartitionSelector implements PartitionSelectorStrategy {
    @Override
    public int selectPartition(Object key, int partitionCount) {
        final var partition = Math.abs(key.hashCode()) % partitionCount;
        log.info("Publishing by key:{}, partition: {}",key,partition);
        return partition;
    }


}

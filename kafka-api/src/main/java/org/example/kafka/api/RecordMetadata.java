package org.example.kafka.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class RecordMetadata {

    @Getter
    private final String topic;
    @Getter
    private final int partition;
    @Getter
    private final long offset;

    @Override
    public String toString() {
        return topic + "-" + partition + "@" + offset;
    }
}

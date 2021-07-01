package org.example.kafka.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class RecordMetadata {

    private final String topic;
    private final int partition;
    private final long offset;
    @Setter
    private Long timestamp = null;

    @Override
    public String toString() {
        return topic + "-" + partition + "@" + offset;
    }
}

package org.example.kafka.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.kafka.api.helper.ReflectionHelper;

@Getter
@RequiredArgsConstructor
public class RecordMetadata {

    private final String topic;
    private final int partition;
    private final long offset;
    private final Long timestamp;

    @Override
    public String toString() {
        return topic + "-" + partition + "@" + offset;
    }

    public static <T> RecordMetadata create(T originalMetadata) {
        final Class<?> clazz = originalMetadata.getClass();
        return new RecordMetadata(
                (String) ReflectionHelper.invoke(clazz, "topic", originalMetadata),
                (int) ReflectionHelper.invoke(clazz, "partition", originalMetadata),
                (long) ReflectionHelper.invoke(clazz, "offset", originalMetadata),
                (long) ReflectionHelper.invoke(clazz, "timestamp", originalMetadata));
    }
}

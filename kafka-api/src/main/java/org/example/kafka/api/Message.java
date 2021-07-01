package org.example.kafka.api;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.kafka.api.helper.ReflectionHelper;

@Getter
@RequiredArgsConstructor
public class Message<K, V> {

    private final K key;
    private final V value;
    private final String topic;
    private final int partition;
    private final long offset;
    private final List<KeyValue> keyValues;

    public static <K, V, T> Message<K, V> create(T originalRecord) {
        final Class<?> clazz = originalRecord.getClass();
        final Object headers = ReflectionHelper.invoke(clazz, "headers", originalRecord);
        final List<KeyValue> keyValues = KeyValue.fromHeaders(
                (Object[]) ReflectionHelper.invoke(headers.getClass(), "toArray", headers));
        return new Message<>((K) ReflectionHelper.invoke(clazz, "key", originalRecord),
                (V) ReflectionHelper.invoke(clazz, "value", originalRecord),
                (String) ReflectionHelper.invoke(clazz, "topic", originalRecord),
                (int) ReflectionHelper.invoke(clazz, "partition", originalRecord),
                (long) ReflectionHelper.invoke(clazz, "offset", originalRecord),
                keyValues);
    }
}

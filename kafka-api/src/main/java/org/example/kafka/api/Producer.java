package org.example.kafka.api;

import java.util.Map;
import java.util.concurrent.Future;

public interface Producer<K, V> {

    Future<RecordMetadata> sendAsync(
            String topic, Integer partition, Long timestamp, K key, V value, Map<String, byte[]> properties);

    default Future<RecordMetadata> sendAsync(String topic, Integer partition, Long timestamp, K key, V value) {
        return sendAsync(topic, partition, timestamp, key, value, null);
    }

    default Future<RecordMetadata> sendAsync(String topic, Integer partition, K key, V value) {
        return sendAsync(topic, partition, null, key, value, null);
    }

    default Future<RecordMetadata> sendAsync(String topic, K key, V value) {
        return sendAsync(topic, null, null, key, value, null);
    }

    default Future<RecordMetadata> sendAsync(String topic, V value) {
        return sendAsync(topic, null, null, null, value, null);
    }

    void close();
}

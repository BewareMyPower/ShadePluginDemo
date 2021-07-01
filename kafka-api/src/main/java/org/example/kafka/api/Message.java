package org.example.kafka.api;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Message<K, V> {

    private final K key;
    private final V value;
    private final String topic;
    private final int partition;
    private final long offset;
    private final Map<String, byte[]> properties = new HashMap<>();

    public void addProperty(String key, byte[] value) {
        properties.put(key, value);
    }

    public byte[] getProperty(String key) {
        return properties.get(key);
    }
}

package org.example.kafka.api;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public interface Consumer<K, V> {

    void subscribe(Collection<String> topics);

    default void subscribe(String topic) {
        subscribe(Collections.singleton(topic));
    }

    List<Message<K, V>> receive(long timeoutMs);

    void close();
}

package org.example.kafka.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import lombok.Builder;
import lombok.Getter;

@Builder
public class MessageBuilder<K, V> {

    private Producer<K, V> producer;
    private String topic;
    private K key;
    private V value;
    private Integer partition;
    private Long timestamp;
    private List<KeyValue> keyValues;

    @Getter
    private final CompletableFuture<RecordMetadata> future = new CompletableFuture<>();

    public <T, H> T toProducerRecord(Class<T> clazz, BiFunction<String, byte[], H> headerConstructor) {
        try {
            final Constructor<T> constructor = clazz.getConstructor(String.class, Integer.class, Long.class,
                    Object.class, Object.class, Iterable.class);
            return constructor.newInstance(topic, partition, timestamp, key, value,
                    KeyValue.toHeaders(keyValues, headerConstructor));
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> void complete(T metadata, Exception e) {
        if (e == null) {
            future.complete(RecordMetadata.create(metadata));
        } else {
            future.completeExceptionally(e);
        }
    }

    public Future<RecordMetadata> sendAsync() {
        if (producer == null) {
            throw new IllegalArgumentException("producer is null");
        }
        return producer.sendAsync(this);
    }
}

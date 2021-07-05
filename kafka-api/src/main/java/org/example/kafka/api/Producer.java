package org.example.kafka.api;

import java.util.concurrent.Future;

public interface Producer<K, V> extends AutoCloseable {

    Future<RecordMetadata> sendAsync(MessageBuilder<K, V> context);

    default MessageBuilder.MessageBuilderBuilder<K, V> newContextBuilder(final String topic, final V value) {
        return MessageBuilder.<K, V>builder()
                .producer(this)
                .topic(topic)
                .value(value);
    }
}

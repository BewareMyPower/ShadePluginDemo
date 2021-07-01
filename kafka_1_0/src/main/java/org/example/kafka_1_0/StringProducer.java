package org.example.kafka_1_0;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.api.KeyValue;
import org.example.kafka.api.Producer;
import org.example.kafka.api.RecordMetadata;

public class StringProducer extends KafkaProducer<String, String> implements Producer<String, String> {

    public StringProducer(final Properties properties) {
        super(properties);
    }

    @Override
    public Future<RecordMetadata> sendAsync(
            String topic, Integer partition, Long timestamp, String key, String value, List<KeyValue> keyValues) {
        CompletableFuture<RecordMetadata> future = new CompletableFuture<>();
        send(new ProducerRecord<>(
                topic, partition, timestamp, key, value, KeyValue.toHeaders(keyValues, RecordHeader::new)),
                ((recordMetadata, e) -> {
                    if (e == null) {
                        future.complete(RecordMetadata.create(recordMetadata));
                    } else {
                        future.completeExceptionally(e);
                    }
                }));
        return future;
    }
}

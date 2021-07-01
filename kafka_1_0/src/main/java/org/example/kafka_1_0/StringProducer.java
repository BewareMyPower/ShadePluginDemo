package org.example.kafka_1_0;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.api.Producer;
import org.example.kafka.api.RecordMetadata;

public class StringProducer extends KafkaProducer<String, String> implements Producer<String, String> {

    public StringProducer(final Properties properties) {
        super(properties);
    }

    @Override
    public Future<RecordMetadata> sendAsync(
            String topic, Integer partition, Long timestamp, String key, String value, Map<String, byte[]> properties) {
        List<Header> headers = (properties == null) ? null : properties.entrySet().stream()
                .map(entry -> new RecordHeader(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        CompletableFuture<RecordMetadata> future = new CompletableFuture<>();
        send(new ProducerRecord<>(topic, partition, timestamp, key, value, headers), (recordMetadata, e) -> {
            if (e == null) {
                final RecordMetadata metadata = new RecordMetadata(
                        recordMetadata.topic(),
                        recordMetadata.partition(),
                        recordMetadata.offset());
                metadata.setTimestamp(recordMetadata.timestamp());
                future.complete(metadata);
            } else {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}

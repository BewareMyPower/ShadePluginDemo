package org.example.kafka_2_0;

import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.example.kafka.api.Producer;
import org.example.kafka.api.MessageBuilder;
import org.example.kafka.api.RecordMetadata;

public class StringProducer extends KafkaProducer<String, String> implements Producer<String, String> {

    public StringProducer(final Properties properties) {
        super(properties);
    }

    @Override
    public Future<RecordMetadata> sendAsync(MessageBuilder<String, String> messageBuilder) {
        send(messageBuilder.toProducerRecord(ProducerRecord.class, RecordHeader::new), messageBuilder::complete);
        return messageBuilder.getFuture();
    }
}

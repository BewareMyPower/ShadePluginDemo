package org.example.kafka_1_0;

import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class StringProducer extends KafkaProducer<String, String> {

    public StringProducer(final String bootstrapServers) {
        super(createProperties(bootstrapServers));
    }

    public Future<RecordMetadata> sendAsync(final String topic, final String value) {
        return send(new ProducerRecord<>(topic, value));
    }

    private static Properties createProperties(final String bootstrapServers) {
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }
}

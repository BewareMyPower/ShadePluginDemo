package org.example.kafka.client.wrapper;

import java.util.Properties;
import lombok.AllArgsConstructor;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.ConsumerConfiguration;
import org.example.kafka.api.Producer;
import org.example.kafka.api.ProducerConfiguration;

@AllArgsConstructor
public class ClientFactory {

    public static final String KAFKA_0_11_0 = "0-11-0";
    public static final String KAFKA_1_0_0 = "1-0-0";
    public static final String KAFKA_2_0_0 = "2-0-0";

    private final String version;

    private String getStringSerializer() {
        return String.format("org.apache.kafka-%s.common.serialization.StringSerializer", version);
    }

    private String getStringDeserializer() {
        return String.format("org.apache.kafka-%s.common.serialization.StringDeserializer", version);
    }

    public Producer<String, String> createProducer(String bootstrapServers) {
        final Properties props = ProducerConfiguration.builder()
                .bootstrapServers(bootstrapServers)
                .keySerializer(getStringSerializer())
                .valueSerializer(getStringSerializer())
                .build()
                .toProperties();

        switch (version) {
            case KAFKA_0_11_0:
                return new org.example.kafka_0_11_0.StringProducer(props);
            case KAFKA_1_0_0:
                return new org.example.kafka_1_0.StringProducer(props);
            case KAFKA_2_0_0:
                return new org.example.kafka_2_0.StringProducer(props);
            default:
                throw new IllegalArgumentException("No producer for version: " + version);
        }
    }

    public Consumer<String, String> createConsumer(String bootstrapServers, String groupId, boolean fromEarliest) {
        final Properties props = ConsumerConfiguration.builder()
                .bootstrapServers(bootstrapServers)
                .groupId(groupId)
                .keyDeserializer(getStringDeserializer())
                .valueDeserializer(getStringDeserializer())
                .fromEarliest(fromEarliest)
                .build()
                .toProperties();

        switch (version) {
            case KAFKA_0_11_0:
                return new org.example.kafka_0_11_0.StringConsumer(props);
            case KAFKA_1_0_0:
                return new org.example.kafka_1_0.StringConsumer(props);
            case KAFKA_2_0_0:
                return new org.example.kafka_2_0.StringConsumer(props);
            default:
                throw new IllegalArgumentException("No producer for version: " + version);
        }
    }
}

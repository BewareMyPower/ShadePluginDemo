package org.example.kafka.client.wrapper;

import lombok.AllArgsConstructor;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.Producer;

@AllArgsConstructor
public class ClientFactory {

    public static final String KAFKA_1_0_0 = "1.0.0";
    public static final String KAFKA_2_0_0 = "2.0.0";

    private final String version;

    public Producer<String, String> createProducer(String bootstrapServers) {
        if (version.equals(KAFKA_1_0_0)) {
            return new org.example.kafka_1_0.StringProducer(bootstrapServers);
        } else if (version.equals(KAFKA_2_0_0)) {
            return new org.example.kafka_2_0.StringProducer(bootstrapServers);
        } else {
            throw new IllegalArgumentException("No producer for version: " + version);
        }
    }

    public Consumer<String, String> createConsumer(String bootstrapServers, String groupId) {
        if (version.equals(KAFKA_1_0_0)) {
            return new org.example.kafka_1_0.StringConsumer(bootstrapServers, groupId);
        } else if (version.equals(KAFKA_2_0_0)) {
            return new org.example.kafka_2_0.StringConsumer(bootstrapServers, groupId);
        } else {
            throw new IllegalArgumentException("No producer for version: " + version);
        }
    }
}

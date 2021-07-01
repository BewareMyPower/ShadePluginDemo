package org.example.kafka.api;

import java.util.Properties;
import lombok.Builder;

@Builder
public class ConsumerConfiguration {

    String bootstrapServers;
    String groupId;
    Object keyDeserializer;
    Object valueDeserializer;
    boolean fromEarliest = false;

    public Properties toProperties() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.deserializer", keyDeserializer);
        props.put("value.deserializer", valueDeserializer);
        props.put("group.id", groupId);
        props.put("auto.offset.reset", fromEarliest ? "earliest" : "latest");
        return props;
    }
}

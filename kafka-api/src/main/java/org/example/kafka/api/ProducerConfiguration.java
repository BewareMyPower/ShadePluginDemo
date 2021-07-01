package org.example.kafka.api;

import java.util.Properties;
import lombok.Builder;

@Builder
public class ProducerConfiguration {

    String bootstrapServers;
    Class<?> keySerializer;
    Class<?> valueSerializer;

    public Properties toProperties() {
        final Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);
        return props;
    }
}

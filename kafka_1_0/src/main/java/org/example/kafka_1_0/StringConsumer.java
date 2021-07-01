package org.example.kafka_1_0;

import java.util.ArrayList;
import java.util.List;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.ConsumerConfiguration;
import org.example.kafka.api.Message;

public class StringConsumer extends KafkaConsumer<String, String> implements Consumer<String, String> {

    public StringConsumer(String bootstrapServers, String groupId) {
        super(ConsumerConfiguration.builder()
                .bootstrapServers(bootstrapServers)
                .groupId(groupId)
                .fromEarliest(true)
                .keyDeserializer(StringDeserializer.class)
                .valueDeserializer(StringDeserializer.class)
                .build()
                .toProperties());
    }

    @Override
    public List<Message<String, String>> receive(long timeoutMs) {
        final List<Message<String, String>> messages = new ArrayList<>();
        poll(timeoutMs).forEach(record -> {
            final Message<String, String> message = new Message<>(
                    record.key(),
                    record.value(),
                    record.topic(),
                    record.partition(),
                    record.offset());
            record.headers().forEach(header -> message.addProperty(header.key(), header.value()));
            messages.add(message);
        });
        return messages;
    }
}

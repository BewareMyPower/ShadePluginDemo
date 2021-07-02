package org.example.kafka_0_11_0;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.Message;

public class StringConsumer extends KafkaConsumer<String, String> implements Consumer<String, String> {

    public StringConsumer(final Properties properties) {
        super(properties);
    }

    @Override
    public List<Message<String, String>> receive(long timeoutMs) {
        final List<Message<String, String>> messages = new ArrayList<>();
        poll(timeoutMs).forEach(record -> messages.add(Message.create(record)) );
        return messages;
    }
}

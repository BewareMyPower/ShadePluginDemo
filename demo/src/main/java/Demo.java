import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.KeyValue;
import org.example.kafka.api.Message;
import org.example.kafka.api.Producer;
import org.example.kafka.api.RecordMetadata;
import org.example.kafka.client.wrapper.ClientFactory;

public class Demo {

    private static void processMessage(Message<String, String> message) {
        final List<KeyValue> keyValues = message.getKeyValues();
        System.out.println("Receive " + message.getValue() + " from " + message.getOffset());
        if (message.getKey() != null) {
            System.out.println("  key: " + message.getKey());
        }
        if (keyValues != null) {
            keyValues.forEach(keyValue -> System.out.println("  " + keyValue));
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final String bootstrapServers = "localhost:9092";
        final String topic = "my-topic";

        final ClientFactory clientFactory_1_0_0 = new ClientFactory(ClientFactory.KAFKA_1_0_0);
        final ClientFactory clientFactory_2_0_0 = new ClientFactory(ClientFactory.KAFKA_2_0_0);

        final Producer<String, String> producer1 = clientFactory_1_0_0.createProducer(bootstrapServers);
        RecordMetadata recordMetadata = producer1.sendAsync(topic, "kafka 1.0.0").get();
        System.out.println("Send to " + recordMetadata);
        recordMetadata = producer1.sendAsync(topic, 0, null, "key-1-0-0", "value-1-0-0",
                Collections.singletonList(new KeyValue("k1", "v1".getBytes(StandardCharsets.UTF_8)))).get();
        System.out.println("Send to " + recordMetadata);
        producer1.close();

        final Producer<String, String> producer2 = clientFactory_2_0_0.createProducer(bootstrapServers);
        recordMetadata = producer2.sendAsync(topic, "kafka 2.0.0").get();
        System.out.println("Send to " + recordMetadata);
        recordMetadata = producer2.sendAsync(topic, 0, null, "key-2-0-0", "value-2-0-0",
                Collections.singletonList(new KeyValue("k2", "v2".getBytes(StandardCharsets.UTF_8)))).get();
        System.out.println("Send to " + recordMetadata);
        producer2.close();

        final Consumer<String, String> consumer1 =
                clientFactory_1_0_0.createConsumer(bootstrapServers, "group-1-0-0", true);
        consumer1.subscribe(topic);
        final AtomicInteger numReceived = new AtomicInteger(0);
        while (numReceived.get() < 4) {
            consumer1.receive(1000).forEach(message -> {
                processMessage(message);
                numReceived.incrementAndGet();
            });
        }
        consumer1.close();

        final Consumer<String, String> consumer2 =
                clientFactory_2_0_0.createConsumer(bootstrapServers, "group-2-0-0", true);
        consumer2.subscribe(topic);
        numReceived.set(0);
        while (numReceived.get() < 4) {
            consumer2.receive(1000).forEach(message -> {
                processMessage(message);
                numReceived.incrementAndGet();
            });
        }
        consumer2.close();
    }
}

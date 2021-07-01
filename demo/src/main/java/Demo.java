import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.Producer;
import org.example.kafka.api.RecordMetadata;
import org.example.kafka.client.wrapper.ClientFactory;

public class Demo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final String bootstrapServers = "localhost:9092";
        final String topic = "my-topic";

        final ClientFactory clientFactory_1_0_0 = new ClientFactory(ClientFactory.KAFKA_1_0_0);
        final ClientFactory clientFactory_2_0_0 = new ClientFactory(ClientFactory.KAFKA_2_0_0);

        final Producer<String, String> producer1 = clientFactory_1_0_0.createProducer(bootstrapServers);
        RecordMetadata recordMetadata = producer1.sendAsync(topic, "kafka 1.0.0").get();
        System.out.println("Send to " + recordMetadata);
        producer1.close();

        final Producer<String, String> producer2 = clientFactory_2_0_0.createProducer(bootstrapServers);
        recordMetadata = producer2.sendAsync(topic, "kafka 2.0.0").get();
        System.out.println("Send to " + recordMetadata);
        producer2.close();

        final Consumer<String, String> consumer1 =
                clientFactory_1_0_0.createConsumer(bootstrapServers, "group-1-0-0");
        consumer1.subscribe(topic);
        final AtomicInteger numReceived = new AtomicInteger(0);
        while (numReceived.get() < 2) {
            consumer1.receive(1000).forEach(message -> {
                System.out.println("Receive " + message.getValue() + " from " + message.getOffset());
                numReceived.incrementAndGet();
            });
        }
        consumer1.close();

        final Consumer<String, String> consumer2 =
                clientFactory_2_0_0.createConsumer(bootstrapServers, "group-2-0-0");
        consumer2.subscribe(topic);
        numReceived.set(0);
        while (numReceived.get() < 2) {
            consumer2.receive(1000).forEach(message -> {
                System.out.println("Receive " + message.getValue() + " from " + message.getOffset());
                numReceived.incrementAndGet();
            });
        }
        consumer2.close();
    }
}

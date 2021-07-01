import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import org.example.kafka.api.RecordMetadata;

public class Demo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final String bootstrapServers = "localhost:9092";
        final String topic = "my-topic";

        final org.example.kafka_1_0.StringProducer producer1 =
                new org.example.kafka_1_0.StringProducer(bootstrapServers);
        RecordMetadata recordMetadata = producer1.sendAsync(topic, "kafka 1.0.0").get();
        System.out.println("Send to " + recordMetadata);
        producer1.close();

        final org.example.kafka_2_0.StringProducer producer2 =
                new org.example.kafka_2_0.StringProducer(bootstrapServers);
        recordMetadata = producer2.sendAsync(topic, "kafka 2.0.0").get();
        System.out.println("Send to " + recordMetadata);
        producer2.close();

        final org.example.kafka_1_0.StringConsumer consumer1 =
                new org.example.kafka_1_0.StringConsumer(bootstrapServers, "group-1-0-0");
        consumer1.subscribe(topic);
        final AtomicInteger numReceived = new AtomicInteger(0);
        while (numReceived.get() < 2) {
            consumer1.receive(1000).forEach(message -> {
                System.out.println("Receive " + message.getValue() + " from " + message.getOffset());
                numReceived.incrementAndGet();
            });
        }
        consumer1.close();

        final org.example.kafka_2_0.StringConsumer consumer2 =
                new org.example.kafka_2_0.StringConsumer(bootstrapServers, "group-2-0-0");
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

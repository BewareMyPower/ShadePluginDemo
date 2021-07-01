import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import org.example.kafka.api.RecordMetadata;

public class Demo {
    public static void main(String[] args) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException {
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
    }
}

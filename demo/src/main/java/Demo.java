import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.example.kafka.api.Consumer;
import org.example.kafka.api.KeyValue;
import org.example.kafka.api.Message;
import org.example.kafka.api.Producer;
import org.example.kafka.api.MessageBuilder;
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

    private static final String[] SUPPORTED_VERSIONS = {
            ClientFactory.KAFKA_0_11_0,
            ClientFactory.KAFKA_1_0_0,
            ClientFactory.KAFKA_2_0_0
    };

    public static void main(String[] args) {
        final String bootstrapServers = "localhost:9092";
        final String topic = "my-topic";

        MessageBuilder<String, String> context = MessageBuilder.<String, String>builder()
                .value("hello")
                .build();
        System.out.println(context.getFuture().isDone());

        final Map<String, ClientFactory> clientFactoryMap = Arrays.stream(SUPPORTED_VERSIONS)
                .collect(Collectors.toMap(
                        version -> version,
                        ClientFactory::new,
                        (k, v) -> { throw new IllegalStateException("Duplicated key: " + k); },
                        TreeMap::new
                ));

        // 1. Produce messages
        clientFactoryMap.forEach((version, factory) -> {
            try (Producer<String, String> producer = factory.createProducer(bootstrapServers)) {
                final RecordMetadata metadata =
                        producer.newContextBuilder(topic, "message-" + version).build().sendAsync().get();
                System.out.println("Kafka producer " + version + " send to " + metadata);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 2. Consume messages
        clientFactoryMap.forEach((version, factory) -> {
            try (Consumer<String, String> consumer =
                         factory.createConsumer(bootstrapServers, "group-" + version, true)) {
                consumer.subscribe(topic);
                int numReceived = 0;
                System.out.println("# Kafka consumer " + version + " started receiving...");
                while (numReceived < SUPPORTED_VERSIONS.length) {
                    final List<Message<String, String>> messages = consumer.receive(1000);
                    numReceived += messages.size();
                    messages.forEach(Demo::processMessage);
                }
                System.out.println("# Kafka consumer " + version + " done.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class Demo {
    private static final List<String> KAFKA_VERSIONS = Arrays.asList("1-0-0", "2-0-0");
    private static final Map<String, Method> RECORD_OFFSET_METHODS = new HashMap<>();

    static {
        for (String version : KAFKA_VERSIONS) {
            final String className = String.format("org.apache.kafka-%s.clients.producer.RecordMetadata", version);
            try {
                final Class<?> clazz = Demo.class.getClassLoader().loadClass(className);
                final Method method = clazz.getMethod("offset");
                RECORD_OFFSET_METHODS.put(version, method);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                System.err.println("Cannot load offset() method for class: " + className);
                e.printStackTrace();
                Runtime.getRuntime().halt(1);
            }
        }
    }

    private static long getOffset(Object recordMetadata, String version) {
        try {
            return (long) RECORD_OFFSET_METHODS.get(version).invoke(recordMetadata);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException, InvocationTargetException, IllegalAccessException {
        final String bootstrapServers = "localhost:9092";
        final String topic = "my-topic";

        Object recordMetadata = null;

        final org.example.kafka_1_0.StringProducer producer1 =
                new org.example.kafka_1_0.StringProducer(bootstrapServers);
        recordMetadata = producer1.sendAsync(topic, "kafka 1.0.0").get();
        System.out.println(getOffset(recordMetadata, "1-0-0"));
        producer1.close();

        final org.example.kafka_2_0.StringProducer producer2 =
                new org.example.kafka_2_0.StringProducer(bootstrapServers);
        recordMetadata = producer2.sendAsync(topic, "kafka 2.0.0").get();
        System.out.println(getOffset(recordMetadata, "2-0-0"));

        producer2.close();
    }
}

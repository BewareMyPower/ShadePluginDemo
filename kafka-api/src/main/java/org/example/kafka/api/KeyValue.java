package org.example.kafka.api;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.kafka.api.helper.ReflectionHelper;

@AllArgsConstructor
@Getter
public class KeyValue {

    private final String key;
    private final byte[] value;

    private static KeyValue fromHeader(Object originalHeader) {
        final Class<?> clazz = originalHeader.getClass();
        return new KeyValue(
                (String) ReflectionHelper.invoke(clazz, "key", originalHeader),
                (byte[]) ReflectionHelper.invoke(clazz, "value", originalHeader));
    }

    public static List<KeyValue> fromHeaders(List<Object> originalHeaders) {
        if (originalHeaders == null) {
            return null;
        }
        return originalHeaders.stream().map(KeyValue::fromHeader).collect(Collectors.toList());
    }

    public static <T> List<T> toHeaders(List<KeyValue> keyValues, BiFunction<String, byte[], T> constructor) {
        if (keyValues == null) {
            return null;
        }
        return keyValues.stream()
                .map(keyValue -> constructor.apply(keyValue.getKey(), keyValue.getValue()))
                .collect(Collectors.toList());
    }
}

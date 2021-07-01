package org.example.kafka.api.helper;

import java.lang.reflect.InvocationTargetException;

public class ReflectionHelper {

    public static Object invoke(Class<?> clazz, String name, Object object) {
        try {
            return clazz.getMethod(name).invoke(object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }
}

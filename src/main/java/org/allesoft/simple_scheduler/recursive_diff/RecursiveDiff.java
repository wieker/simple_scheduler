package org.allesoft.simple_scheduler.recursive_diff;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class A {
    private int a;
}

class B extends A {
    private int b;
}

public class RecursiveDiff {
    public static void main(Object a, Object b) {
        Class<?> componentClass = a.getClass();
        List<String> lines = new ArrayList<>();

        do {
            Field[] fields = componentClass.getDeclaredFields();
            processField(a, lines, fields, b);
            componentClass = componentClass.getSuperclass();
        } while (componentClass != null);

        for (String line : lines) {
            System.out.println(line);
        }
    }

    private static void processField(Object object, List<String> lines, Field[] fields, Object b) {
        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                boolean compared = (field.get(object) == field.get(b)) || (field.get(object) != null && field.get(object).equals(field.get(b)));
                lines.add(field.getName() + " = " + compared);
                if (!compared && field.get(object) != null && field.get(b) != null) {
                    main(field.get(object), field.get(b));
                }
            } catch (final IllegalAccessException e) {
                lines.add(field.getName() + " > " + e.getClass().getSimpleName());
            }
        });
    }
}

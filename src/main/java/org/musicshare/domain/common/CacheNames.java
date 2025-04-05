package org.musicshare.domain.common;

import java.lang.reflect.Field;
import static java.lang.reflect.Modifier.isStatic;
import java.util.ArrayList;
import java.util.List;

public class CacheNames {
    public static final String SEPARATOR = ":";

    public static final String MEMBER = "member";

    public static List<String> getCacheNames() {
        List<String> cacheNames = new ArrayList<>();

        Field[] declaredFields = CacheNames.class.getDeclaredFields();
        for (Field field : declaredFields) {
            if (isStatic(field.getModifiers())) {
                try {
                    if (!SEPARATOR.equals(field.get(null))) {
                        cacheNames.add((String) field.get(null));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return cacheNames;
    }
}

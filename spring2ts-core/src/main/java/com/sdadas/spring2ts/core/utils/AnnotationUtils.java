package com.sdadas.spring2ts.core.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.AnnotationTarget;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Sławomir Dadas
 */
public final class AnnotationUtils {

    @SafeVarargs
    public static boolean hasAny(AnnotationTarget<?> target, Class<? extends Annotation>... types) {
        for (Class<? extends Annotation> type : types) {
            if(target.getAnnotation(type) != null) return true;
        }
        return false;
    }

    public static Multimap<String, String> getAnnotationAsMap(AnnotationTarget<?> target,
                                                              Class<? extends Annotation> type) {
        org.jboss.forge.roaster.model.Annotation<?> annotation = target.getAnnotation(type);
        return getAnnotationAsMap(annotation, type);
    }

    public static Multimap<String, String> getAnnotationAsMap(org.jboss.forge.roaster.model.Annotation<?> annotation,
                                                        Class<? extends Annotation> type) {

        if(annotation == null) return null;
        List<Method> methods = getAnnotationAttributes(type);
        Multimap<String, String> result = HashMultimap.create();
        for (Method method : methods) {
            String name = method.getName();
            boolean array = method.getReturnType().isArray();
            String value = annotation.getLiteralValue(name);
            if(StringUtils.isBlank(value)) continue;
            String [] values = array
                    ? annotation.getStringArrayValue(name) : new String[]{annotation.getStringValue(name)};
            if(allElementsNull(values)) continue;
            result.putAll(name, Arrays.asList(values));
        }
        return result;
    }

    private static boolean allElementsNull(Object [] values) {
        for (Object value : values) {
            if(value != null) return false;
        }
        return true;
    }

    private static List<Method> getAnnotationAttributes(Class<? extends Annotation> type) {
        Stream<Method> methods = Arrays.stream(type.getDeclaredMethods());
        List<Method> results = methods.filter(AnnotationUtils::isAttributeMethod).collect(Collectors.toList());
        results.forEach(r -> r.setAccessible(true));
        return results;
    }

    private static boolean isAttributeMethod(Method method) {
        return (method != null && method.getParameterTypes().length == 0 && method.getReturnType() != void.class);
    }

    private AnnotationUtils() {
    }
}

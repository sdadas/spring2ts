package com.sdadas.spring2ts.examples.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author Sławomir Dadas
 */
public final class JsonUtils {

    private final static ObjectMapper MAPPER = createMapper();

    private static ObjectMapper createMapper() {
        ObjectMapper result = new ObjectMapper();
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return result;
    }

    /**
     * Zwraca sformatowany obiekt json.
     *
     * @param object object
     * @return String
     */
    public static String toJson(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public static <T> T toObject(Class<T> clazz, String json) {
        try {
            return MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private JsonUtils() {
    }
}

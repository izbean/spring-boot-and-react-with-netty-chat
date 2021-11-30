package com.izbean.springbootnettychat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static <T> T readValueOrThrow(String value, Class<T> cls) {
        try {
            return objectMapper.readValue(value, cls);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static <T> String writeValueAsStringOrThrow(T cls) {
        try {
            return objectMapper.writeValueAsString(cls);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}

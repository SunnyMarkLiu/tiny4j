package com.markliu.tiny4j.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * author:sunnymarkliu
 * date  :16-9-22
 * time  :上午10:54
 */
public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将 POJO 转为 Json 串
     */
    public static <T> String toJson(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("convert " + object + " to json error!", e);
            return "";
        }
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(json, cls);
        } catch (IOException e) {
            LOGGER.error("convert " + json + " to POJO(" + cls.getName() +") error!", e);
            return null;
        }
    }
}

package com.markliu.tiny4j.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties 文件读取的工具类
 * author:sunnymarkliu
 * date  :16-9-14
 * time  :下午8:17
 */
public final class PropertiesUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    /**
     * 加载属性文件
     * @param fileName 属性文件名称
     * @return Properties
     */
    public static Properties loadProperties(String fileName) {

        Properties properties = null;
        InputStream inStream = null;
        try {
            inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if (inStream == null)
                throw new FileNotFoundException(fileName + " is not found!");

            properties = new Properties();
            properties.load(inStream);
        } catch (IOException e) {
            LOGGER.error("load properties file" + fileName + " failure!", e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.error("close inputstream failure!", e);
                }
            }
        }
        return properties;
    }

    /**
     * 根据 key 获取属性值
     * @param properties 属性文件所对应的 {@code Properties} 对象
     * @param key 键值
     * @param defaultValue 默认键值
     * @return 键值所对应的值
     */
    public static String getStringValue(Properties properties, String key, String defaultValue) {
        String value = defaultValue;
        if (properties.containsKey(key)) {
            value = properties.getProperty(key);
        }
        return value;
    }

    /**
     * 根据 key 获取属性值
     * @param properties 属性文件所对应的 {@code Properties} 对象
     * @param key 键值
     * @return 键值所对应的值
     */
    public static String getStringValue(Properties properties, String key) {
        return getStringValue(properties, key, "");
    }

    /**
     * 根据 key 获取属性值
     * @param properties 属性文件所对应的 {@code Properties} 对象
     * @param key 键值
     * @return 键值所对应的值，默认为 0
     */
    public static int getIntValue(Properties properties, String key) {
        if (properties.containsKey(key)) {
            String value = properties.getProperty(key);
            if (StringUtils.isNotEmpty(value)) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    LOGGER.error("cast number exception", e);
                    return 0;
                }
            }
        }
        return 0;
    }
}

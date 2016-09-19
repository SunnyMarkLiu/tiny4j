package com.markliu.tiny4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午5:04
 */
public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 初始化创建类
     */
    public static Object newInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            LOGGER.error("load class error!", e);
            throw new RuntimeException(e);
        }
    }
}

package com.markliu.tiny4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

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

    /**
     * 设置属性值
     *
     * @param object 所要对属性赋值的对象
     * @param field  需要赋值的属性字段
     * @param value  设置的值
     */
    public static void setField(Object object, Field field, Object value) {
        try {
            // 设置属性可访问
            field.setAccessible(true);
            // 设置属性值
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

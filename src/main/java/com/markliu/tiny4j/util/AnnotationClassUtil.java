package com.markliu.tiny4j.util;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Service;
import com.markliu.tiny4j.ioc.ClassScanLoadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * 获取 Controller 和 Service 注解类的工具类
 *
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午3:44
 */
public class AnnotationClassUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationClassUtil.class);

    private static final Set<Class<?>> ALL_CLASS_SET;

    static {
        ALL_CLASS_SET = ClassScanLoadUtil.getAllClassSet();
    }

    /**
     * 获取所有标有 @Controller 注解的类
     */
    public static Set<Class<?>> getControllers() {
        Set<Class<?>> controllerSet = new HashSet<Class<?>>();
        for (Class<?> cls : ALL_CLASS_SET) {
            // 检查是否标记 Controller
            if (cls.getAnnotation(Controller.class) != null) {
                controllerSet.add(cls);
            }
        }
        return controllerSet;
    }

    /**
     * 获取所有标有 @Service 注解的类
     */
    public static Set<Class<?>> getServices() {
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> cls : ALL_CLASS_SET) {
            // 检查是否标记 Controller
            if (cls.getAnnotation(Service.class) != null) {
                serviceSet.add(cls);
            }
        }
        return serviceSet;
    }

    /**
     * 获取所有标有 @Controller, @Service 注解的类
     */
    public static Set<Class<?>> getAnnotationClassSet() {
        Set<Class<?>> annotationSet = new HashSet<Class<?>>();
        annotationSet.addAll(getControllers());
        annotationSet.addAll(getServices());
        return annotationSet;
    }
}

package com.markliu.tiny4j.ioc;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Service;
import com.markliu.tiny4j.ioc.ClassScanLoadUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取 Controller 和 Service 注解类的工具类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午3:44
 */
public class AnnotationClassUtil {

    private static final Set<Class<?>> ALL_CLASS_SET;

    static {
        ALL_CLASS_SET = ClassScanLoadUtil.getAllClassSet();
    }

    /**
     * 获取所有标有 @Controller 注解的类
     */
    public static Set<Class<?>> getControllers() {
        return getClassSetByAnnotation(Controller.class);
    }

    /**
     * 获取所有标有 @Service 注解的类
     */
    public static Set<Class<?>> getServices() {
        return getClassSetByAnnotation(Service.class);
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

    /**
     * 获取所有标注 annotation 指定注解的类
     */
    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for (Class<?> cls : ALL_CLASS_SET) {
            // 检查是否标记 Controller
            if (cls.getAnnotation(annotation) != null) {
                classSet.add(cls);
            }
        }
        return classSet;
    }
}

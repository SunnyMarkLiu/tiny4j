package com.markliu.tiny4j.util;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Service;
import com.markliu.tiny4j.ioc.ClassScanLoadUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 获取 Controller 和 Service 注解类的工具类
 *
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
     * 获取所有标有 @Service 注解的类及对应的名称 value 值
     */
    public static Map<String, Class<?>> getServicesAndNames() {
        Map<String, Class<?>> serviceMap = new HashMap<String, Class<?>>();
        for (Class<?> cls : ALL_CLASS_SET) {
            Service service = cls.getAnnotation(Service.class);
            if (service != null) {
                String serviceName = service.value();
                if (StringUtil.isNotEmpty(serviceName) && !serviceMap.containsKey(serviceName)) {
                    serviceMap.put(serviceName, cls);
                }
            }
        }
        return serviceMap;
    }

    /**
     * 获取所有标有 @Controller 注解的类及对应的名称 value 值
     */
    public static Map<String, Class<?>> getControllersAndNames() {
        Map<String, Class<?>> controllerMap = new HashMap<String, Class<?>>();
        for (Class<?> cls : ALL_CLASS_SET) {
            Controller controller = cls.getAnnotation(Controller.class);
            if (controller != null) {
                String controllerName = controller.value();
                if (StringUtil.isNotEmpty(controllerName) && !controllerMap.containsKey(controllerName)) {
                    controllerMap.put(controllerName, cls);
                }
            }
        }
        return controllerMap;
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
     * 获取所有标有 @Controller, @Service 注解的类及其名称 value 值
     */
    public static Map<String, Class<?>> getAnnotationClassNameMap() {
        Map<String, Class<?>> annotationClassNameMap = new HashMap<String, Class<?>>();
        annotationClassNameMap.putAll(getControllersAndNames());
        annotationClassNameMap.putAll(getServicesAndNames());
        return annotationClassNameMap;
    }
}

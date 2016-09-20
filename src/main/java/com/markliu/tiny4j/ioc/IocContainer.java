package com.markliu.tiny4j.ioc;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Service;
import com.markliu.tiny4j.util.AnnotationClassUtil;
import com.markliu.tiny4j.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * IoC 容器管理工具类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午5:03
 */
public class IocContainer {

    /**
     * beanclass - bean 容器
     */
    private static final Map<Class<?>, Object> BEAN_CONTAINER = new HashMap<Class<?>, Object>();

    /**
     * beanname - bean 容器
     */
    private static final Map<String, Object> BEAN_CLASSNAME_CONTAINER = new HashMap<String, Object>();

    static {
        createIocContainer();
    }

    /**
     * 获取应用包下的所有的 Controller、Service 类，实例化，添加到 IoC 容器中。
     * 注意 Controller、Service 类的单例，不能重复初始化
     */
    private static void createIocContainer() {
        Set<Class<?>> annotationClassSet = AnnotationClassUtil.getAnnotationClassSet();
        for (Class<?> beanClass : annotationClassSet) {
            Object bean = ReflectionUtil.newInstance(beanClass);
            BEAN_CONTAINER.put(beanClass, bean);

            // 设置 BEAN_CLASSNAME_CONTAINER 容器的值
            Controller controller = beanClass.getAnnotation(Controller.class);
            Service service = beanClass.getAnnotation(Service.class);

            if (controller != null) {
                String controllerName = controller.value();
                BEAN_CLASSNAME_CONTAINER.put(controllerName, bean);
            }
            if (service != null) {
                String serviceName = service.value();
                BEAN_CLASSNAME_CONTAINER.put(serviceName, bean);
            }
        }
    }

    /**
     * 根据类型从容器中获取该对象实例
     *
     * @param classType 类的 class 类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanByClass(Class<T> classType) {
        if (!BEAN_CONTAINER.containsKey(classType)) {
            throw new RuntimeException("can't get bean by class " + classType);
        }
        return (T) BEAN_CONTAINER.get(classType);
    }

    /**
     * 根据类型从容器中获取该对象实例
     *
     * @param className 类的名称
     */
    public static Object getBeanByClassName(String className) {
        if (!BEAN_CLASSNAME_CONTAINER.containsKey(className)) {
            throw new RuntimeException("can't get bean by classname " + className);
        }
        return BEAN_CLASSNAME_CONTAINER.get(className);
    }

}

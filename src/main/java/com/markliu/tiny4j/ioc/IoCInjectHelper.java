package com.markliu.tiny4j.ioc;

import com.markliu.tiny4j.annotation.Inject;
import com.markliu.tiny4j.util.ArrayUtil;
import com.markliu.tiny4j.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * IoC 容器中实现对 Controller 和 Service 的属性的依赖注入。
 * 思路：
 * 1. 容器中获取 Controller 和 Service 实例化的对象
 * 2. 遍历对象的属性 Field，如果标记有 Inject 注解，则从容器获取该注解的对象，注入到该属性中
 * <p>
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :上午10:38
 */
public class IoCInjectHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(IoCInjectHelper.class);

    /*
        静态代码块实现依赖的注入
     */
    static {
        LOGGER.info("IoC Inject.");
        // 从容器中获取所有标记 Controller 和 Service 的类
        Map<Class<?>, Object> ioCBeanMap = IocContainer.getIoCBeanMap();

        if (ioCBeanMap != null && !ioCBeanMap.isEmpty()) {
            for (Map.Entry<Class<?>, Object> entry : ioCBeanMap.entrySet()) {
                // 容器中 bean 的类型
                Class<?> beanClass = entry.getKey();
                // 容器中的 bean 实例
                Object bean = entry.getValue();
                Field[] fields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)) {
                    // 遍历对象的属性 Field
                    for (Field field : fields) {
                        // 如果标记有 Inject 注解，则从容器获取该注解的对象，注入到该属性中
                        if (field.isAnnotationPresent(Inject.class)) {
                            Class<?> injectClass = field.getType();
                            // 从容器获取该注解的对象
                            Object injectBean = IocContainer.getBeanByClass(injectClass);
                            if (injectBean != null) {
                                // 容器中的 bean 实例中的属性设置值
                                ReflectionUtil.setField(bean, field, injectBean);
                            }
                        }
                    }
                }
            }
        }

    }
}

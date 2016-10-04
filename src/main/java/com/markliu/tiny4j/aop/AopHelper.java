package com.markliu.tiny4j.aop;

import com.markliu.tiny4j.ioc.AnnotationClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Aop 加载工具类
 *
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :下午12:07
 */
public class AopHelper {

    /**
     * 获取某一 Aspect 注解所标注的所要代理的目标类
     *
     * @param aspect Aspect 注解，value 标注的所要代理的目标类
     */
    private static Set<Class<?>> getProxyTargetClass(Aspect aspect) {

        Set<Class<?>> classSet = new HashSet<Class<?>>();
        // 该 Aspect 所要代理的目标类
        Class<Annotation> targetAnnotation = aspect.value();
        if (!targetAnnotation.equals(Aspect.class)) {
            Set<Class<?>> classes = AnnotationClassUtil.getClassSetByAnnotation(targetAnnotation);
            classSet.addAll(classes);
        }
        return classSet;
    }

    /**
     * 获取代理类与所代理的目标类集合的映射
     */
    private static Map<Class<?>, Set<Class<?>>> getProxyTargetClassSetMap() {

        // 遍历代理类集合，获取每个代理类所要代理的目标类集合

        Map<Class<?>, Set<Class<?>>> proxyTargetsMap = new HashMap<Class<?>, Set<Class<?>>>();

        // 获取所有的代理类（继承 AspectProxy 并包含 Aspect 注解）
        Set<Class<?>> proxyClassSet = AnnotationClassUtil.getClassSetByAnnotation(Aspect.class);
        // 判断是否继承自 AspectProxy
        for (Class<?> proxyClass : proxyClassSet)
            if (AspectProxy.class.isAssignableFrom(proxyClass)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                // 获取该 aspect 类所代理的目标类集合
                Set<Class<?>> classSet = getProxyTargetClass(aspect);
                proxyTargetsMap.put(proxyClass, classSet);
            }
        return proxyTargetsMap;
    }
}

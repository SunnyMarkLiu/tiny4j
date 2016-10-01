package com.markliu.tiny4j.aop;

import com.markliu.tiny4j.ioc.AnnotationClassUtil;
import com.markliu.tiny4j.ioc.IocContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * Aop 加载工具类
 *
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :下午12:07
 */
public class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    /*
     * 静态代码块初始化 AOP 框架
     */
    static {
        try {
            // 获取目标类与代理类列表的映射关系，为后续执行链式代理
            Map<Class<?>, List<Proxy>> targetProxyMap = getTargetProxyClassSetMap();
            for (Map.Entry<Class<?>, List<Proxy>> targetProxyEntry : targetProxyMap.entrySet()) {
                Class<?> targetClass = targetProxyEntry.getKey();
                List<Proxy> proxyList = targetProxyEntry.getValue();
                // 针对目标类及其代理类列表创建代理对象
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                IocContainer.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop initial failure!", e);
        }
    }

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

    /**
     * 获取目标类所对应的代理类集合，为后续执行链式代理准备
     */
    private static Map<Class<?>, List<Proxy>> getTargetProxyClassSetMap() throws Exception{

        Map<Class<?>, List<Proxy>> targetProxyMap = new HashMap<Class<?>, List<Proxy>>();

        Map<Class<?>, Set<Class<?>>> proxyTargetsMap = getProxyTargetClassSetMap();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyTargetsMap.entrySet()) {
            Class<?> proxyClass = proxyEntry.getKey();
            Set<Class<?>> targetsClassSet = proxyEntry.getValue();
            for (Class targetClass : targetsClassSet) {
                // 实例化代理对象
                Proxy proxy = (Proxy) proxyClass.newInstance();
                // 完成目标类与代理类列表的映射
                if (targetProxyMap.containsKey(targetClass)) {
                    targetProxyMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetProxyMap.put(targetClass, proxyList);
                }
            }
        }
        return targetProxyMap;
    }
}

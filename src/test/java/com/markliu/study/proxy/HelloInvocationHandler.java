package com.markliu.study.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * author:sunnymarkliu
 * date  :16-9-23
 * time  :下午8:17
 */
public class HelloInvocationHandler implements InvocationHandler {

    private Object target;

    public HelloInvocationHandler(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        System.out.println(this.getClass().getName() + " before ...");
        Object result = method.invoke(target, args);
        System.out.println(this.getClass().getName() + " after ...");
        return result;
    }

    /**
     * 创建代理类
     */
    public static Object createProxy(Object target) {

        Class<?> targetClass = target.getClass();
        return Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                targetClass.getInterfaces(),
                new HelloInvocationHandler2(target)
        );
    }
}

package com.markliu.study.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * author:sunnymarkliu
 * date  :16-9-23
 * time  :下午9:24
 */
public class WorldProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();

    @SuppressWarnings("unchecked")
    public <T> T getProxyBean(Class<T> cls) {

        enhancer.setSuperclass(cls);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    public Object intercept(Object target,
                            Method method,
                            Object[] args,
                            MethodProxy methodProxy) throws Throwable {

        System.out.println(this.getClass().getName() + ":" + method.getName() +" before ...");
        Object result = methodProxy.invokeSuper(target, args);
        System.out.println(this.getClass().getName() + ":" + method.getName() +" after ...");
        return result;
    }
}

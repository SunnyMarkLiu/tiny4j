package com.markliu.tiny4j.aop;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :上午9:53
 */
public class ProxyManager {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {

        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 拦截目标类的方法
             */
            public Object intercept(Object targetObject,
                                    Method targetMethod,
                                    Object[] methodParams,
                                    MethodProxy methodProxy) throws Throwable {


                // 当拦截到目标类的方法时，执行链式代理，返回最终的结果
                return new ProxyChain(targetObject, targetMethod, methodParams,
                        methodProxy, targetClass, proxyList).doProxyChain();
            }
        });
    }
}

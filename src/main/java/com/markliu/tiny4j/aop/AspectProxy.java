package com.markliu.tiny4j.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 *
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :上午10:29
 */
public abstract class AspectProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(Aspect.class);
    /**
     * 模板方法
     * @param proxyChain 代理链
     * @return 执行链式代理和目标类的方法后的返回结果
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result = null;

        Class<?> targetClass = proxyChain.getTargetClass();
        Object targetObject = proxyChain.getTargetObject();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();

        beginProxy(targetClass, targetObject, targetMethod, methodParams);
        try {
            // 是否启用代理（拦截器）
            if (enableIntercept(targetClass, targetObject, targetMethod, methodParams)) {
                beforeProxy(targetClass, targetObject, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                afterProxy(targetClass, targetObject, targetMethod, methodParams, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LOGGER.error("doProxy failure", e);
            error(targetClass, targetObject, targetMethod, methodParams);
            throw e;
        } finally {
            endProxy(targetClass, targetObject, targetMethod, methodParams);
        }
        return result;
    }

    /**
     * 发生异常执行的操作，即异常拦截器
     */
    protected void error(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams) {
    }

    /**
     * 执行拦截之后
     */
    protected void afterProxy(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams, Object result) {

    }

    /**
     * 开始执行拦截之前
     */
    protected void beforeProxy(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams) {

    }

    /**
     * 开始执行代理之前的操作，如开启日志等
     */
    protected void beginProxy(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams) {

    }

    protected void endProxy(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams) {

    }

    /**
     * 钩子方法，是否启用代理（拦截器）默认开启
     */
    protected boolean enableIntercept(Class<?> targetClass, Object targetObject, Method targetMethod, Object[] methodParams) {
        return true;
    }
}

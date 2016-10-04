package com.markliu.tiny4j.aop;

import com.markliu.tiny4j.util.CollectionUtil;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :上午9:51
 */
public class ProxyChain {

    private Object targetObject;
    private Method targetMethod;
    private Object[] methodParams;

    /**
     * 所拦截的方法的代理
     */
    private MethodProxy methodProxy;

    private Class<?> targetClass;
    private List<Proxy> proxyList;
    /**
     * 当前执行的 Proxy 在代理链中的下标
     */
    private int doProxyIndex;

    public ProxyChain(Object targetObject, Method targetMethod,
                      Object[] methodParams, MethodProxy methodProxy,
                      Class<?> targetClass, List<Proxy> proxyList) {

        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodParams = methodParams;
        this.methodProxy = methodProxy;
        this.targetClass = targetClass;
        this.proxyList = proxyList;
    }

    /**
     * 执行链式代理操作
     * @return 返回最终链式代理的结果
     */
    public Object doProxyChain() throws Throwable {

        Object result = null;
        if (CollectionUtil.isNotEmpty(proxyList)) {

            // 如果目标类之前还有代理，则执行代理的操作
            if (doProxyIndex < proxyList.size()) {
                result = proxyList.get(doProxyIndex).doProxy(this);
            } else { // 代理链执行完毕，执行目标类的方法
                result = methodProxy.invokeSuper(targetObject, methodParams);
            }
        }
        return result;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public MethodProxy getMethodProxy() {
        return methodProxy;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public List<Proxy> getProxyList() {
        return proxyList;
    }
}

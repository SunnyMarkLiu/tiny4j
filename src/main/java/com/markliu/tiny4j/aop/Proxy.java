package com.markliu.tiny4j.aop;

/**
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :上午9:48
 */
public interface Proxy {

    /**
     * 链式执行代理
     * @param proxyChain 代理链
     * @return 执行代理所返回的结果
     */
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}

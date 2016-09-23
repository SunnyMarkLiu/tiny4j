package com.markliu.study.proxy;

import org.junit.Test;

/**
 * author:sunnymarkliu
 * date  :16-9-23
 * time  :下午8:27
 */
public class TestProxy {

    @Test
    public void testProxy() {
        Hello hello = new HelloImpl();

        Hello helloProxy = (Hello) HelloInvocationHandler.createProxy(
                HelloInvocationHandler2.createProxy(hello)
        );

        helloProxy.say("Jack");
    }
}

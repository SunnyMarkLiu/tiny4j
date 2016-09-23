package com.markliu.study.cglib;

import org.junit.Test;

/**
 * author:sunnymarkliu
 * date  :16-9-23
 * time  :下午9:39
 */
public class CglibTest {

    @Test
    public void testCglib() {
        HelloProxy helloProxy = new HelloProxy();
        Hello hello = helloProxy.getProxyBean(Hello.class);
        hello.say("Jack");

        WorldProxy worldProxy = new WorldProxy();
        World world = worldProxy.getProxyBean(World.class);
        world.sayWorld("world");
    }
}

package com.markliu.study.proxy;

/**
 * author:sunnymarkliu
 * date  :16-9-23
 * time  :下午8:26
 */
public class HelloImpl implements Hello {

    public void say(String name) {
        System.out.println("hello " + name);
    }
}

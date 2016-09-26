package com.markliu.study.aspectj;

import org.springframework.stereotype.Component;

/**
 * author:sunnymarkliu
 * date  :16-9-26
 * time  :下午9:26
 */
@Component
public class GreetingImpl implements Greeting {

    public String sayHello(String name) {
        System.out.println("hello " + name);
        return "hello " + name;
    }

    @Tag
    public void goodMethod() {
        System.out.println("goodMethod");
    }
}

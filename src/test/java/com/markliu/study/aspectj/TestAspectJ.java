package com.markliu.study.aspectj;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * author:sunnymarkliu
 * date  :16-9-26
 * time  :下午9:40
 */
public class TestAspectJ {

    @Test
    public void testAspectJ() {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Greeting greeting = (Greeting) context.getBean("greetingImpl");
        String result = greeting.sayHello("Sunny");
        System.out.println("result: " + result);

        System.out.println("=============================");

        GreetingImpl greetingImpl = (GreetingImpl) greeting;
        greetingImpl.goodMethod();

        System.out.println("=============================");
        Apology apology = (Apology) greeting;
        apology.saySorry("MarkLiu");

    }
}

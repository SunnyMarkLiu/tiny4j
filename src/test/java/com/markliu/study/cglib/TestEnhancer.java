package com.markliu.study.cglib;

import junit.framework.Assert;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.InvocationHandler;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * author:sunnymarkliu
 * date  :16-9-24
 * time  :下午9:11
 */
public class TestEnhancer {

    @Test
    public void testFixedCallBack() {

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);

        enhancer.setCallback(new FixedValue() {

            // 此处修改返回值
            public Object loadObject() throws Exception {
                return "Hello Enhancer";
            }
        });

        // create 方法创建代理对象
        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test("world"));
        System.out.println(proxy.test2("world"));
        // ClassCastException
        // System.out.println(proxy.hashCode());

        // fianl 修饰的方法不会拦截
        System.out.println(proxy.getClass());

    }

    @Test
    public void testInvocationHandler() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // proxy.hashCode(); // StackOverflowError
                System.out.println(this.getClass().getName() + " before ...");
                // 此种方法无法反射调用 method 方法
                // Object result = method.invoke(??, args);
                System.out.println(this.getClass().getName() + " after ...");
                return "result";
            }

        });

        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test("world"));
    }
}

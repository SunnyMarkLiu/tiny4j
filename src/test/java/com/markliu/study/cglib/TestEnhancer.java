package com.markliu.study.cglib;

import junit.framework.Assert;
import net.sf.cglib.proxy.*;
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

    @Test
    public void testMethodInterceptor() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new MethodInterceptor() {

            public Object intercept(Object target, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println(this.getClass().getName() + " before ...");
                System.out.println(method.getDeclaringClass() + "." + method.getName());

                // 反射调用拦截父类的方法
                Object result = methodProxy.invokeSuper(target, args);
                System.out.println(this.getClass().getName() + " after ...");
                return result;
            }

        });

        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test("world"));
    }

    @Test
    public void testLazyLoader() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new LazyLoader() {

            public Object loadObject() throws Exception {

                return new SampleClass("test");
            }
        });

        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test("world"));
    }

    @Test
    public void testDispatcher() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SampleClass.class);
        enhancer.setCallback(new Dispatcher() {
            public Object loadObject() throws Exception {
                return new SampleClass("test");
            }
        });

        SampleClass proxy = (SampleClass) enhancer.create();
        System.out.println(proxy.test("world"));
        // 由于不会 stored 不会保存生成的对象, 所以调用另一个方法会重新调用loadObject方法创建对象。
        System.out.println(proxy.test2("world"));
    }
}

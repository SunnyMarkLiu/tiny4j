package com.markliu.study.cglib;

import junit.framework.Assert;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.beans.ImmutableBean;
import net.sf.cglib.core.Converter;
import net.sf.cglib.proxy.*;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
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

    @Test
    public void testImmutableBean() {
        SampleClass sampleClass = new SampleClass();
        sampleClass.setName("Sunny");
        System.out.println("sampleClass.getName():" + sampleClass.getName());

        // 创建 ImmutableBean
        SampleClass immutableBean = (SampleClass) ImmutableBean.create(sampleClass);
        System.out.println("immutableBean.getName():" + immutableBean.getName());
        // sampleClass 修改状态同时会修改 immutableBean 的状态
        sampleClass.setName("SunnyLiu");
        System.out.println("immutableBean.getName():" + immutableBean.getName());
        // java.lang.IllegalStateException: Bean is immutable
        immutableBean.setName("Liu");
    }

    /**
     * 生成指定类的实例
     */
    @Test
    public void testBeanGenerator() throws Exception {
        BeanGenerator beanGenerator = new BeanGenerator();
        // 生成指定类的实例
        beanGenerator.setSuperclass(SampleClass.class);
        Object bean = beanGenerator.create();

        Method setName = bean.getClass().getMethod("setName", String.class);
        Object result = setName.invoke(bean, "Sunny");
        System.out.println("result:" + result);

        Method getName = bean.getClass().getMethod("getName");
        Object name = getName.invoke(bean);
        System.out.println("name:" + name);
    }

    /**
     * 动态指定类的属性等
     */
    @Test
    public void testBeanGenerator2() throws Exception {
        BeanGenerator beanGenerator = new BeanGenerator();
        // 动态指定属性
        beanGenerator.addProperty("name", String.class);
        Object bean = beanGenerator.create();

        Method setName = bean.getClass().getMethod("setName", String.class);
        Object result = setName.invoke(bean, "Sunny");
        System.out.println("result:" + result);

        Method getName = bean.getClass().getMethod("getName");
        Object name = getName.invoke(bean);
        System.out.println("name:" + name);
    }

    @Test
    public void testBeanCopier() {
        BeanCopier copier = BeanCopier.create(SampleClass.class, OtherSampleClass.class, true);
        SampleClass bean = new SampleClass();
        bean.setName("Sunny");
        bean.setAge(20);
        OtherSampleClass otherBean = new OtherSampleClass();
        copier.copy(bean, otherBean, new Converter() {

            public Object convert(Object value, Class target, Object context) {

                System.out.println("需要复制的属性值:" + value);
                System.out.println("修改需要复制的属性值...");
                System.out.println("需要待复制的类的属性:" + target.getName());
                // 对不同属性进行复制
                if (target.getName().equals(Integer.class.getName())) {
                    value = 24;
                } else if (target.getName().equals(String.class.getName())){
                    value = "other value";
                }
                return value;
            }
        });
        System.out.println("name:" + otherBean.getName());
        System.out.println("age:" + otherBean.getAge());
    }

    @Test
    public void testMap() {
        SampleClass bean = new SampleClass();
        bean.setName("Sunny");
        bean.setAge(20);
        BeanMap beanMap = BeanMap.create(bean);

        System.out.println("name:" + beanMap.get("name"));
        System.out.println("age:" + beanMap.get("age"));
    }
}

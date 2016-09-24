# CGLib basic tutorial
## Enhancer
`Enhancer`允许创建接口类/非接口类的代理类，Java 的 standard library 自带的类`Proxy`用于创建实现指定接口的代理类。
`Enhancer`创建的动态代理类可以拦截指定类的所有方法的调用。

Demo：待创建的代理类 POJO:

```
public /*final*/ class SampleClass {

    public String test(String input) {

        return "Hello " + input;
    }

    public String test2(String input) {

        return "input: " + input;
    }

    @Override
    public String toString() {
        return "SampleClass{}";
    }

    @Override
    public int hashCode() {
        System.out.println("hashCode");
        return super.hashCode();
    }
}
```

### Callback：FixedValue
使用 `cglib` 中 `Enhancer` 的 `FixedValue callback` 可以轻松的修改原有类方法的返回值：

```
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
```
以上代码 `enhancer` 会生成 `SampleClass` 的子类，调用 `SampleClass` 类的所有方法都会返回固定的值！

- 调用 `Enhancer#create(Object...)` 方法，接受可变长参数，用于选择构造函数创建指定类的对象；
- 调用 `Enhancer#createClass` 方法只会创建指定类的 `Class` 实例，而不会初始化创建对象，使用找个`Class`可以动态创建实例；

注意:
- 所有方法的调用都会委派到动态生成的子类对象中，包括 `Object` 中的方法。所以，调用 `proxy.toString()` 方法也会返回
`Hello cglib!`，对于 `proxy.hashCode()`或其他返回类型与 `loadObject` 的返回类型不同的方法，也会拦截，不过会报 `ClassCastException` 异常；
- `final` 修饰的方法不会被拦截，例如调用 `proxy.getClass()` 方法会返回类似于 `SampleClass$$EnhancerByCGLIB$$852b4c42`；
- `final` 修饰的类不能动态生成子类，`Cannot subclass final class`；
- 动态生成的类与给定的类在同一个包中；

### Callback：InvocationHandler
`InvocationHandler` more powerful callback class！是 `java.lang.reflect.InvocationHandler` 的替代品。
```
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
```
注意：
- `InvocationHandler` 不能实现反射调用被代理类的方法，因为没有传入被代理类的实例(`target`)，无法调用 `method.invoke(target, args)`；
- 此处不能使用 `method.invoke(proxy, args)`，因为 `proxy` 所有方法（除`final` 修饰的方法）的调用会被委派到 `invoke` 中，会造成无限循环，`StackOverflowError`；
- 要实现拦截方法的执行，使用另一个 callback dispatcher：`MethodInterceptor`；

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

### Callback：MethodInterceptor
another powerful callback dispatcher：`MethodInterceptor`
```
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
```
`MethodInterceptor` 可是实现拦截方法的全面控制，可以在拦截方法的直线前后进行其他操作、修改拦截的方法的返回结果等。但一般会发现许多人使用其他的方法，
原因是其他的方法可能更有效率，而在使用 cglib 的框架中效率起着至关重要的作用。
其他常用的 Callback：
1. `LazyLoader`
- LazyLoader 的 `loadObject` 方法与 `FixedValue` 的方法签名相同，但用途完全不同。`LazyLoader` 的 `loadObject` 方法用于返回被动态生成（`enhanced class`）类的子类，而
`FixedValue` 返回的是所拦截的方法返回的值。
- `LazyLoader` 懒加载模式生成的对象，只有在一个方法调用时创建该对象。所以 `LazyLoader` 常用于创建对象所需代价较大（expensive）但使用未知的类。
- 注意 `enhanced class` 的有些构造方法在代理类和懒加载生成的类中都会调用。所以可以他通过 `Enhancer#create(Object...)` 选择构造函数，注意需要提供默认构造函数。
```
    public /*final*/ class SampleClass {
    
        public SampleClass() {
            System.out.println("SampleClass()...");
        }
    
        public SampleClass(String name) {
            System.out.println("SampleClass()..." + name);
        }
        ...
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
```
输出：（会调用默认构造函数）
```
SampleClass()...
SampleClass()...test
Hello world
```
2. `Dispatcher`
类似于 `LazyLoader` ，但不同的是，调用方法时`Dispatcher` 将会 invoked ，但不会保存生成的对象。
```
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
```
输出：
```
SampleClass()...
SampleClass()...test
Hello world
SampleClass()...test
input: world
```
## Immutable Bean 不可变类
使用 `ImmutableBean` 可以创建一个不可变类，当试图调用方法修改不可变类的属性时，会报 `IllegalStateException`：
```
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
```
输出：
```
SampleClass()...
sampleClass.getName():Sunny
SampleClass()...
immutableBean.getName():Sunny
immutableBean.getName():SunnyLiu

java.lang.IllegalStateException: Bean is immutable
    ...
```
如上面的例子所示，`ImmutableBean`会阻止所有状态信息的改变，抛出 `java.lang.IllegalStateException: Bean is immutable` 异常。

## Bean Generator 
`BeanGenerator` 用于在运行时实例化对象。
```
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
```
输出：
```
testBeanGenerator:
    SampleClass()...
    result:null
    name:Sunny
    
testBeanGenerator2:
    result:null
    name:Sunny
```

## Bean Copier 
`BeanCopier` 用于根据属性值复制 bean。可以通过指定 `Converter` 进行相关复制前的转换操作。
```
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
```
输出：
```
需要复制的属性值:20
修改需要复制的属性值...
需要待复制的类的属性:java.lang.Integer
需要复制的属性值:Sunny
修改需要复制的属性值...
需要待复制的类的属性:java.lang.String
name:other value
age:24
```

## Bean Map
`BeanMap` 用于将将对象的所有属性转换为 `String`-to-`Object` 形式的 `Map` 集合。
```
    @Test
    public void testMap() {
        SampleClass bean = new SampleClass();
        bean.setName("Sunny");
        bean.setAge(20);
        BeanMap beanMap = BeanMap.create(bean);

        System.out.println("name:" + beanMap.get("name"));
        System.out.println("age:" + beanMap.get("age"));
    }
```
输出：
```
name:Sunny
age:20
```
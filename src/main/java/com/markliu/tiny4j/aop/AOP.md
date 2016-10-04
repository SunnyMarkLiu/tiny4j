# 开发 AOP 框架基本思路
- Aspect 注解，标注一个切面类，或者说一个代理类，value 属性为所要代理的类（注解） 如 Controller；
- Proxy 接口，代理类的接口，doProxy(ProxyChain proxyChain) 执行代理方法，调用链接代理；
- ProxyChain 类，执行链式代理，维护代理类列表；
```
    public Object doProxyChain() throws Throwable {

        Object result = null;
        if (CollectionUtil.isNotEmpty(proxyList)) {

            // 如果目标类之前还有代理，则执行代理的操作
            if (doProxyIndex < proxyList.size()) {
                result = proxyList.get(doProxyIndex).doProxy(this);
            } else { // 代理链执行完毕，执行目标类的方法
                result = methodProxy.invokeSuper(targetObject, methodParams);
            }
        }
        return result;
    }
```

- AspectProxy 类，抽象切面类，定义拦截器的模板方法。对于切面类，需要继承 AspectProxy 并且添加 Aspect 注解
```
public abstract class AspectProxy implements Proxy {

    public Object doProxy(ProxyChain proxyChain) throws Throwable {

        Object result = null;
        ...
        
        beginProxy(targetClass, targetObject, targetMethod, methodParams);
        try {
            // 是否启用代理（拦截器）
            if (enableIntercept(targetClass, targetObject, targetMethod, methodParams)) {
                beforeProxy(targetClass, targetObject, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                afterProxy(targetClass, targetObject, targetMethod, methodParams, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Exception e) {
            LOGGER.error("doProxy failure", e);
            error(targetClass, targetObject, targetMethod, methodParams);
            throw e;
        } finally {
            endProxy(targetClass, targetObject, targetMethod, methodParams);
        }
        return result;
    }
```

- ProxyManager 类，创建代理，createProxy(Class<?> targetClass, List<Proxy> proxyList)；
```
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {

        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            /**
             * 拦截目标类的方法
             */
            public Object intercept(Object targetObject,
                                    Method targetMethod,
                                    Object[] methodParams,
                                    MethodProxy methodProxy) throws Throwable {
                // 当拦截到目标类的方法时，执行链式代理，返回最终的结果
                return new ProxyChain(targetObject, targetMethod, methodParams,
                        methodProxy, targetClass, proxyList).doProxyChain();
            }
        });
    }
```
由上面的两个方法可知，需要获取 proxyList ，即针对某一目标类的所有代理切面类，目标类可能有多个，所以需要获取这样一个
Map：Map<Class target, List<Proxy> proxyList> 。然后传入 target 和 对应的 proxyList 创建 target 的代理类。
因此需要:
    1. 获取所有的 target 类，由于 value 指定的是注解类，所以是 Controller 或 Service 等类，后续会通过 IoC 初始化;
    2. 获取所有 Aspect 类（继承 AspectProxy 并包含 Aspect 注解）;
    3. 遍历 Aspect 类 集合，获取对应的所要代理的目标类集合（步骤1）,并实现 aspect 与 targetSet 的映射
    3. 获取实例化所有 Aspect 类;


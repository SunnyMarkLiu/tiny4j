package com.markliu.tiny4j.http;

import com.markliu.tiny4j.annotation.RequestMapping;
import com.markliu.tiny4j.annotation.RequestMethod;
import com.markliu.tiny4j.ioc.AnnotationClassUtil;
import com.markliu.tiny4j.util.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 请求与响应的映射。
 * 解析 Controller 类的 RequestMapping 和 方法的 RequestMapping，构成请求路径，
 * 解析获取请求方法，封装成 Request 对象，与对应的 Controller 的处理方法完成映射。
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午6:46
 */
public class HandlerMapping {

    private static final Map<Request, Handler> REQUEST_HANDLER_MAP = new HashMap<Request, Handler>();

    static {
        // 获取所有的 Controller 类
        Set<Class<?>> controllers = AnnotationClassUtil.getControllers();
        if (CollectionUtil.isNotEmpty(controllers)) {
            for (Class<?> controller : controllers) {
                RequestMapping typeAnnotation = controller.getAnnotation(RequestMapping.class);
                String requestTypePath = "";
                if (typeAnnotation != null) {
                    requestTypePath = typeAnnotation.value();
                }
                Method[] methods = controller.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        // 如果请求方法包含 RequestMapping 注解
                        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                        if (mapping != null) {
                            String requestPath = mapping.value();
                            RequestMethod[] requestMethods = mapping.method();
                            // 构建完整的请求路径
                            requestPath = requestTypePath + requestPath;
                            // 创建 Request 对象
                            Request request = new Request(requestPath, requestMethods);
                            // 构建响应的 Handler 对象
                            Handler handler = new Handler(controller, method);
                            // 实现 Request 与 Handler 的映射
                            REQUEST_HANDLER_MAP.put(request, handler);
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据请求对象获取对应的响应类
     */
    public static Handler getRequestHandler(String requestPath, String requestMethod) {

        RequestMethod m = RequestMethodUtil.castRequestMethod(requestMethod);
        RequestMethod[] methods = {m};
        Request request = new Request(requestPath, methods);
        AssertUtil.notNull(request, "request can not be null!");

        for (Request requestKey : REQUEST_HANDLER_MAP.keySet()) {
            if (requestKey.getRequestPath().equals(request.getRequestPath())) {
                RequestMethod[] requestKeyMethods = requestKey.getRequestMethods();
                RequestMethod[] requestMethods = request.getRequestMethods();
                for (RequestMethod method : requestMethods) {
                    boolean contain = false;
                    for (RequestMethod keyMethod : requestKeyMethods) {
                        if (method.equals(keyMethod)) {
                            contain = true;
                        }
                    }
                    if (!contain)
                        return null;
                }
                request = requestKey;
            }
        }
        return REQUEST_HANDLER_MAP.get(request);
    }
}

package com.markliu.tiny4j.http;

import java.lang.reflect.Method;

/**
 * 封装响应信息
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午6:45
 */
public class Handler {

    /**
     * 响应对应的 Controller 类
     */
    private Class<?> responseController;

    /**
     * 响应对应的方法
     */
    private Method actionMethod;

    public Handler(Class<?> responseController, Method actionMethod) {
        this.responseController = responseController;
        this.actionMethod = actionMethod;
    }

    public Class<?> getResponseController() {
        return responseController;
    }

    public void setResponseController(Class<?> responseController) {
        this.responseController = responseController;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }
}

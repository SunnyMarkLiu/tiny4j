package com.markliu.tiny4j.http;

import com.markliu.tiny4j.annotation.RequestMethod;

/**
 * 封装请求对象
 * <p>
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午6:43
 */
public class Request {

    /**
     * 请求方法
     */
    private RequestMethod[] requestMethods;

    /**
     * 请求路径
     */
    private String requestPath;

    public Request(String requestPath, RequestMethod[] requestMethods) {
        this.requestMethods = requestMethods;
        this.requestPath = requestPath;
    }

    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    public void setRequestMethods(RequestMethod[] requestMethods) {
        this.requestMethods = requestMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

}

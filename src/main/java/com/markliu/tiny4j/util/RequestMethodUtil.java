package com.markliu.tiny4j.util;

import com.markliu.tiny4j.annotation.RequestMethod;

/**
 * 字符串类型的请求方法与枚举类型转换
 * <p>
 * author:sunnymarkliu
 * date  :16-9-21
 * time  :下午5:49
 */
public class RequestMethodUtil {

    public static RequestMethod castRequestMethod(String requestMethod) {
        if (requestMethod.equalsIgnoreCase("GET"))
            return RequestMethod.GET;
        else if (requestMethod.equalsIgnoreCase("POST"))
            return RequestMethod.POST;
        else if (requestMethod.equalsIgnoreCase("HEAD"))
            return RequestMethod.HEAD;
        else if (requestMethod.equalsIgnoreCase("PUT"))
            return RequestMethod.PUT;
        else if (requestMethod.equalsIgnoreCase("PATCH"))
            return RequestMethod.PATCH;
        else if (requestMethod.equalsIgnoreCase("DELETE"))
            return RequestMethod.DELETE;
        else if (requestMethod.equalsIgnoreCase("OPTIONS"))
            return RequestMethod.OPTIONS;
        else // requestMethod.equalsIgnoreCase("TRACE"))
            return RequestMethod.TRACE;

    }
}

package com.markliu.tiny4j.http;

import java.util.Map;

/**
 * 封装 Controller 的 RequestMapping 标注的方法的请求参数
 * <p>
 * author:sunnymarkliu
 * date  :16-9-21
 * time  :下午9:09
 */
public class ActionMethodParam {

    private Map<String, Object> requestParamMap;

    public ActionMethodParam(Map<String, Object> requestParamMap) {
        this.requestParamMap = requestParamMap;
    }

    public ActionMethodParam(){}

    /**
     * 根据 key 获取请求参数的值, 返回 Object 对象
     */
    public Object getObjectParam(String key) {
        if (!requestParamMap.containsKey(key)) {
            return null;
        }
        return requestParamMap.get(key);
    }
}

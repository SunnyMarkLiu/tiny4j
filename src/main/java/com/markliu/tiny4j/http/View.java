package com.markliu.tiny4j.http;

import java.util.Map;

/**
 * 响应的试图封装类
 * <p>
 * author:sunnymarkliu
 * date  :16-9-22
 * time  :上午10:41
 */
public class View {

    private String viewPath;

    private Map<String, Object> modelData;

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public Map<String, Object> getModelData() {
        return modelData;
    }

    public void setModelData(Map<String, Object> modelData) {
        this.modelData = modelData;
    }

}

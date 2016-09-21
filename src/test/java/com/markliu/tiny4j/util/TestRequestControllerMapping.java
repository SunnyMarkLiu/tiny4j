package com.markliu.tiny4j.util;

import com.markliu.tiny4j.annotation.RequestMethod;
import com.markliu.tiny4j.http.Handler;
import com.markliu.tiny4j.http.Request;
import com.markliu.tiny4j.http.HandlerMapping;
import com.markliu.tiny4j.ioc.IocContainer;
import com.markliu.tiny4j.ioc.IocContainerLoader;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * author:sunnymarkliu
 * date  :16-9-20
 * time  :下午7:44
 */
public class TestRequestControllerMapping {

    @Test
    public void testGetRequestHandler() throws InvocationTargetException, IllegalAccessException {
        // 初始化 IoC 容器s
        IocContainerLoader.initIocContainer();

        RequestMethod[] requestMethods = {RequestMethod.GET};
        Request request = new Request("/customer/testMethod" , requestMethods);

        Handler handler = HandlerMapping.getRequestHandler("/customer/testMethod" , "GET");
        Class<?> responseController = handler.getResponseController();
        Method actionMethod = handler.getActionMethod();

        System.out.println("responseController: " + responseController.getName());
        System.out.println("actionMethod: " + actionMethod.getName());

        Object controller = IocContainer.getBeanByClass(responseController);
        actionMethod.invoke(controller, request);
    }
}

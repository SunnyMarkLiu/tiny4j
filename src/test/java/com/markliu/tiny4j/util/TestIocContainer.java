package com.markliu.tiny4j.util;

import com.markliu.tiny4j.controller.CustomerController;
import com.markliu.tiny4j.ioc.ClassScanLoadUtil;
import com.markliu.tiny4j.ioc.IocContainer;
import org.apache.taglibs.standard.lang.jstl.ImplicitObjects;
import org.junit.Test;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午10:41
 */
public class TestIocContainer {

    @Test
    public void testGetBeanByClass() {
        CustomerController controller = IocContainer.getBeanByClass(CustomerController.class);
        CustomerController customerController = (CustomerController) IocContainer.getBeanByClassName("customerController");
        System.out.println(controller);
        System.out.println(customerController);
        System.out.println(controller == customerController);
    }

    @Test
    public void testIoCinject() {
        ClassScanLoadUtil.loadClass("com.markliu.tiny4j.ioc.IoCInjectHelper", true);
        CustomerController controller = IocContainer.getBeanByClass(CustomerController.class);
        controller.testMethod();
    }
}

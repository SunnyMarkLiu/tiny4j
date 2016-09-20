package com.markliu.tiny4j.controller;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Inject;
import com.markliu.tiny4j.annotation.RequestMapping;
import com.markliu.tiny4j.annotation.RequestMethod;
import com.markliu.tiny4j.http.Request;
import com.markliu.tiny4j.service.CustomerService;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :上午11:43
 */
@Controller("customerController")
@RequestMapping("/customer")
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @RequestMapping(value = "/testMethod", method = {RequestMethod.GET, RequestMethod.POST})
    public void testMethod(Request request) {
        customerService.someServiceMethod();
        System.out.println("RequestPath: " + request.getRequestPath());
        String method = "";
        for (RequestMethod requestMethod : request.getRequestMethods()) {
            method += requestMethod + " ";
        }
        System.out.println("RequestMethods: " + method);
    }
}

package com.markliu.tiny4j.controller;

import com.markliu.tiny4j.annotation.Controller;
import com.markliu.tiny4j.annotation.Inject;
import com.markliu.tiny4j.annotation.RequestMapping;
import com.markliu.tiny4j.annotation.RequestMethod;
import com.markliu.tiny4j.service.UserService;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午4:19
 */
@Controller
public class UserController {

    @Inject
    private UserService userService;

    @RequestMapping(value = "/testMethod", method = {RequestMethod.GET, RequestMethod.POST})
    public void testMethod() {

    }
}

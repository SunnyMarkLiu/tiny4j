package com.markliu.tiny4j.service;

import com.markliu.tiny4j.annotation.Service;
import com.markliu.tiny4j.annotation.Transaction;

/**
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :下午4:19
 */
@Service("userService")
public class UserService {

    @Transaction
    public void someServiceMethod() {
        System.out.println("UserService someServiceMethod");
    }

}

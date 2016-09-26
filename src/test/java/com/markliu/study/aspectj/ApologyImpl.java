package com.markliu.study.aspectj;

import org.springframework.stereotype.Component;

/**
 * author:sunnymarkliu
 * date  :16-9-26
 * time  :下午9:55
 */
@Component
public class ApologyImpl implements Apology {

    public void saySorry(String name) {
        System.out.println("sorry " + name);
    }
}

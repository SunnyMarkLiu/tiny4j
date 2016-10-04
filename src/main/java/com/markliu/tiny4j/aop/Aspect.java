package com.markliu.tiny4j.aop;

import java.lang.annotation.Annotation;

/**
 * 切面类
 * author:sunnymarkliu
 * date  :16-10-4
 * time  :上午9:44
 */
public @interface Aspect {

    /**
     * 切面所要拦截的类，该值也是一个注解
     */
    Class<Annotation> value();
}

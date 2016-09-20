package com.markliu.tiny4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求映射注解类 RequestMapping
 *
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :上午11:24
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * RequestMapping 所映射的请求的方法和路径
     * 如：/index
     */
    String value() default "";

    /**
     * 封装请求方法
     */
    RequestMethod[] method() default {RequestMethod.GET};
}

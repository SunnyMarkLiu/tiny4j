package com.markliu.tiny4j.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 业务注解类 Service
 * author:sunnymarkliu
 * date  :16-9-19
 * time  :上午11:02
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * Service 类的名称
     */
    String value() default "";
}

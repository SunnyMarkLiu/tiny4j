package com.markliu.study.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * 引入增强
 *
 * author:sunnymarkliu
 * date  :16-9-26
 * time  :下午9:57
 */
@Aspect
@Component
public class GreetingIntroductionAspect {

    @DeclareParents(value = "com.markliu.study.aspectj.GreetingImpl", defaultImpl = ApologyImpl.class)
    private Apology apology;
}

package com.markliu.study.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 定义 Aspect 切面类
 *
 * author:sunnymarkliu
 * date  :16-9-26
 * time  :下午9:31
 */
@Aspect
@Component
public class GreetingAspect {

    /**
     * @param joinPoint 切点
     */
    @Around("execution(* com.markliu.study.aspectj.GreetingImpl.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        before("around");
        Object result = joinPoint.proceed();
        after("around");
        return result;
    }

    @Around("execution(* com.markliu.study.aspectj.GreetingImpl.*(..))")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable {
        before("around2");
        Object result = joinPoint.proceed();
        after("around2");
        return result;
    }

    @Around("@annotation(com.markliu.study.aspectj.Tag)")
    public Object around3(ProceedingJoinPoint joinPoint) throws Throwable {
        before("around3");
        Object result = joinPoint.proceed();
        after("around3");
        return result;
    }

    private void before(String method) {
        System.out.println("before..." + method);
    }

    private void after(String method) {
        System.out.println("after..." + method);
    }
}

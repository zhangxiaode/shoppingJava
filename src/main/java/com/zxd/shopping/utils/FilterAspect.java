package com.zxd.shopping.utils;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class FilterAspect {
    @Around("execution(* cn.shinelon.controller.UserController.*(..))")
    public Object AspectHandlerMethod(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("apect start");
        long start=new Date().getTime();
        Object[] args=pjp.getArgs();
        for(Object arg:args) {
            System.out.println("arg is "+arg);
        }
        System.out.println("最后耗时为:"+(new Date().getTime()-start));
        Object obj=pjp.proceed();
        System.out.println("aspect end");
        return obj;
    }
}

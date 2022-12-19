package org.sid.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

@Aspect
public class SecondAspect {

    @Pointcut("execution(* org.sid.test.Application.start(..))")
    public void pc1(){}

    @Before("pc1()")
    public void beforeMain(){
        System.out.println("******************************************************************");
        System.out.println("before Main from SecondAspect");
        System.out.println("******************************************************************");
    }


    @After("pc1()")
    public void aftereMain(){
        System.out.println("******************************************************************");
        System.out.println("after Main from SecondAspect");
        System.out.println("******************************************************************");
    }
    @Around("pc1()")
    public void aroundMain(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("******************************************************************");
        System.out.println("before Main from SecondAspect");
        System.out.println("******************************************************************");
        proceedingJoinPoint.proceed();
        System.out.println("******************************************************************");
        System.out.println("after Main from SecondAspect");
        System.out.println("******************************************************************");
    }
}

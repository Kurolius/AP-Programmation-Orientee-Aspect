package org.sid.springAOP.aspects;

import org.sid.aspects.LoggingAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Component
@Aspect
@EnableAspectJAutoProxy
public class LogAspect {
    long t1,t2;
    Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public LogAspect() throws IOException {
        logger.addHandler(new FileHandler("log2.xml"));
        logger.setUseParentHandlers(false);
    }
    
    //@Around("execution(* metier.*.*(..))")
    @Around("@annotation(Log)")
    public Object log(ProceedingJoinPoint joinPoint) {
        Object result=null;
        t1 = System.currentTimeMillis();
        logger.info("Before ...." + new Date(t1) );
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        t2 = System.currentTimeMillis();
        logger.info("After .... " + new Date(t2));
        logger.info("Execution Duration : "+(t2-t1));
        return result;
    }
}

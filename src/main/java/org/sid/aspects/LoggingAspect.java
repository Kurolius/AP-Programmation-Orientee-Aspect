package org.sid.aspects;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@Aspect
public class LoggingAspect {
    long t1,t2;
    Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public LoggingAspect() throws IOException {
        logger.addHandler(new FileHandler("log.xml"));
        logger.setUseParentHandlers(false);
    }


    @Pointcut("execution(* org.sid.metier.IMetierBanqueImpl.*(..))")
    public void pc1(){}

    @Before("pc1()")
    public void avant(JoinPoint joinPoint){
        logger.info("----------------------------------------------------------------");
        t1 = System.currentTimeMillis();
        logger.info("before method execution "+ joinPoint.getSignature());
    }

    @After("pc1()")
    public void apres(JoinPoint joinPoint){
        logger.info("after method execution "+ joinPoint.getSignature());
        t2 = System.currentTimeMillis();
        logger.info("duree d'excution de la methode est "+ (t2-t1));
        logger.info("----------------------------------------------------------------");
    }

}

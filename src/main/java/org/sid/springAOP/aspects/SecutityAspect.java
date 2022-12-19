package org.sid.springAOP.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Component
@Aspect
@EnableAspectJAutoProxy
public class SecutityAspect {
    @Around(value="@annotation(securedByAspect)",argNames =
            "proceedingJoinPoint,securedByAspect")
    public Object log(ProceedingJoinPoint proceedingJoinPoint, SecuredByAspect
            securedByAspect) {
        String[] roles=securedByAspect.roles();
        boolean authorized=false;
        for (String r:roles){
            if(SecurityContext.hasRole(r)) authorized=true;
        }
        if(!authorized){
            throw new RuntimeException("Not Authorized to " + proceedingJoinPoint.getSignature());
        }
        else {
            try {
                Object o=proceedingJoinPoint.proceed();
                return o;
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        }
    }
}

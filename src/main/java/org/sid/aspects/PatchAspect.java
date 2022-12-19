package org.sid.aspects;

import org.sid.metier.Compte;
import org.sid.metier.IMetierBanqueImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class PatchAspect {

    @Pointcut("execution(* org.sid.metier.IMetierBanqueImpl.retirer(..))")
    public void pc1(){}

    @Around("pc1() &&args(code,mt)")
    public void patch(Long code, double mt, JoinPoint joinPoint, ProceedingJoinPoint
            proceedingJoinPoint) throws Throwable {
        IMetierBanqueImpl metier=(IMetierBanqueImpl) joinPoint.getTarget();
        Compte cp=metier.consulter(code);
        if(cp.getSolde()>mt) {
            Object o = proceedingJoinPoint.proceed();
        }
        else
            throw new RuntimeException("Solde insuffisant");
    }

}

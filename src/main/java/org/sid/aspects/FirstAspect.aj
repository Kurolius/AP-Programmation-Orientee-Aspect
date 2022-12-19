package org.sid.aspects;

public aspect FirstAspect {
    pointcut pc1(): execution(* test.Application.main(..));

//    before():pc1(){
//        System.out.println("----------------------------------------------------------------");
//        System.out.println("before Main from FirstAspect");
//        System.out.println("----------------------------------------------------------------");
//    }
//    after():pc1(){
//        System.out.println("----------------------------------------------------------------");
//        System.out.println("after Main from FirstAspect");
//        System.out.println("----------------------------------------------------------------");
//    }
    void around():pc1(){
        System.out.println("----------------------------------------------------------------");
        System.out.println("before Main from FirstAspect");
        System.out.println("----------------------------------------------------------------");


        proceed();

        System.out.println("----------------------------------------------------------------");
        System.out.println("after Main from FirstAspect");
        System.out.println("----------------------------------------------------------------");
    }
}

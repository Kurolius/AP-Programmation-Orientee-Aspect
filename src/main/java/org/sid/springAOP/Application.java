package org.sid.springAOP;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.sid.springAOP.aspects.SecurityContext;
import org.sid.springAOP.service.Imetier;

@ComponentScan(basePackages = {"org.sid.springAOP"})
public class Application {
    public static void main(String[] args) {
        SecurityContext.authenticate("root","1234",new String[]{"USER"});
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Application.class);
        Imetier imetier = applicationContext.getBean(Imetier.class);
        imetier.process();
        try {
            System.out.println(imetier.compute());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}

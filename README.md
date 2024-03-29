﻿# AP-Programmation-Orientee-Aspect
## Premier Test :

### Aspects :

#### FirstAspect : 

```java

public aspect FirstAspect {
    pointcut pc1(): execution(* org.sid.test.Application.main(..));

    before():pc1(){
        System.out.println("----------------------------------------------------------------");
        System.out.println("before Main from FirstAspect");
        System.out.println("----------------------------------------------------------------");
    }
    after():pc1(){
        System.out.println("----------------------------------------------------------------");
        System.out.println("after Main from FirstAspect");
        System.out.println("----------------------------------------------------------------");
    }
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

```

#### SecondAspect :

```java

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
```

#### test :

![image](https://user-images.githubusercontent.com/84138772/208504599-5d8c71e6-58ce-4501-a69b-b95e5abec356.png)

## Deuxieme Test :

### Code Metier :

#### Compte :

```java

public class Compte {
    private Long code;
    private double solde;

    public Compte(Long code, double solde) {
        this.code = code;
        this.solde = solde;
    }

    public Compte() {
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    @Override
    public String toString() {
        return "Compte{" +
                "code=" + code +
                ", solde=" + solde +
                '}';
    }
}


```

#### Interface IMetierBanque :

```java

public interface IMetierBanque {
    void addcompte(Compte cp);
    void verser(Long code,double montant);
    void retirer(Long code,double montant);
    Compte consulter(Long code);
}

```

#### IMetierBanqueImpl :

```java

public class IMetierBanqueImpl implements IMetierBanque {
    Map<Long,Compte> compteMap = new HashMap<>();
    @Override
    public void addcompte(Compte cp) {
        compteMap.put(cp.getCode(),cp);
    }

    @Override
    public void verser(Long code, double montant) {
        Compte compte = compteMap.get(code);
        compte.setSolde(compte.getSolde() + montant);

    }

    @Override
    public void retirer(Long code, double montant) {
        Compte compte = compteMap.get(code);
        compte.setSolde(compte.getSolde() - montant);
    }

    @Override
    public Compte consulter(Long code) {
        return compteMap.get(code);
    }
}


```

### LoggingAspect :

```java
@Aspect
public class LoggingAspect {
    long t1,t2;
    Logger logger = Logger.getLogger(LoggingAspect.class.getName());

    public LoggingAspect() throws IOException {
        logger.addHandler(new FileHandler("log.xml"));
        logger.setUseParentHandlers(false);
    }


    @Pointcut("execution(* metier.IMetierBanqueImpl.*(..))")
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


```

### SecurityAspect :

```java

@Aspect
public class SecurityAspect {
    private String username="root";
    private String password="1234";

    @Pointcut("execution(* test.Application.start(..))")
    public void pc1(){}

    @Around("pc1()")
    public void secureApp(JoinPoint joinPoint, ProceedingJoinPoint proceedingJoinPoint) throws
            Throwable {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Authentication");
        System.out.print("Username :");String username=scanner.next();
        System.out.print("Password :");String pass=scanner.next();
        if(username.equals("root")&&pass.equals("1234")){
            System.out.println("Before starting");
            proceedingJoinPoint.proceed();
            System.out.println("End of Application");
        }
        else{
            System.out.println("Access denied ...");
        }
    }
}


```

### PatchAspect :

```java

@Aspect
public class PatchAspect {

    @Pointcut("execution(* metier.IMetierBanqueImpl.retirer(..))")
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


```
### main application :

```java

public class Application {
    public static void main(String[] args) {
        System.out.println("Message from Main Application");
        new Application().start();
    }
    public void start(){
        System.out.println("Demarrage de l'application");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Donner le code de compte");
        Long code = scanner.nextLong();
        System.out.println("Donner le solde initiale de compte");
        double solde = scanner.nextDouble();

        IMetierBanque iMetierBanque = new IMetierBanqueImpl();
        iMetierBanque.addcompte(new Compte(code,solde));

        while (true){
            try {
                System.out.println("=========================================");
                System.out.println(iMetierBanque.consulter(code).toString());
                System.out.print("Type Opération:");
                String type=scanner.next();
                if(type.equals("q")) break;
                System.out.print("Montant:");
                double montant=scanner.nextDouble();
                if(type.toLowerCase().equals("v"))
                    iMetierBanque.verser(code,montant);
                else if(type.toLowerCase().equals("r"))
                    iMetierBanque.retirer(code,montant);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}


```

### Result :

![image](https://user-images.githubusercontent.com/84138772/208508698-3fc750a8-1edf-44f2-b128-b43a390713a6.png)

## Spring AOP :

### Code Metier :

#### Interface Imetier :

```java

public interface Imetier {
    public void process();
    public double compute();
}

```
#### ImetierImpl :

```java

@Service
public class ImetierImpl implements Imetier {
    @Override
    @Log
    @SecuredByAspect(roles = {"ADMIN","USER"})
    public void process() {
        System.out.println("Business processing ...");
    }

    @Override
    @Log
    @SecuredByAspect(roles = {"ADMIN"})
    public double compute() {
        double data=78;
        System.out.println("Business Computing and returning result ....");
        return data;
    }
}

```
### Annotations :

#### Log :

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
    public @interface Log {
}

```

#### SecuredByAspect :

```java

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SecuredByAspect {
    String[] roles();
}


```
### LogAspect :

```java

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


```
### SecutityAspect :

```java

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


```

### SecurityContext :

```java

public class SecurityContext {
    private static String username="";
    private static String password="";
    private static String[] roles={};
    public static void authenticate(String u,String p,String[] inputRoles){
        if((u.equals("root"))&&(p.equals("1234")) ){
            username= u;password=p;
            roles=inputRoles;
        }
        else throw new RuntimeException("Invalid Credentials..");
    }
    public static boolean hasRole(String role){
        for (String r:roles) {
            if(r.equals(role))return true;
        }
        return false;
    }
}

```

### Main :

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

### Test avec USER : 

![image](https://user-images.githubusercontent.com/84138772/208515367-cac5ae8b-9e64-418e-8124-a08dc300b1b3.png)

### Test avec ADMIN :

![image](https://user-images.githubusercontent.com/84138772/208515488-3f3a5f2a-c31f-4576-9f0d-8110ceac9050.png)

### logs: 
```xml 

<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE log SYSTEM "logger.dtd">
<log>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788095</millis>
  <sequence>0</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>Before ....Mon Dec 19 21:29:48 WEST 2022</message>
</record>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788102</millis>
  <sequence>1</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>After .... Mon Dec 19 21:29:48 WEST 2022</message>
</record>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788102</millis>
  <sequence>2</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>Execution Duration : 8</message>
</record>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788103</millis>
  <sequence>3</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>Before ....Mon Dec 19 21:29:48 WEST 2022</message>
</record>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788104</millis>
  <sequence>4</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>After .... Mon Dec 19 21:29:48 WEST 2022</message>
</record>
<record>
  <date>2022-12-19T21:29:48</date>
  <millis>1671481788104</millis>
  <sequence>5</sequence>
  <logger>org.sid.aspects.LoggingAspect</logger>
  <level>INFO</level>
  <class>org.sid.springAOP.aspects.LogAspect</class>
  <method>log</method>
  <thread>1</thread>
  <message>Execution Duration : 1</message>
</record>
</log>

```


package org.sid.springAOP.aspects;

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

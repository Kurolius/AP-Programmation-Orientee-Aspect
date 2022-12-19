package org.sid.springAOP.service;

import org.springframework.stereotype.Service;
import org.sid.springAOP.aspects.Log;
import org.sid.springAOP.aspects.SecuredByAspect;

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

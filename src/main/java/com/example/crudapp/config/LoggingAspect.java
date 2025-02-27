package com.example.crudapp.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.crudapp.service.*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBeforeMethodExecution() {
        System.out.println("Executing service method...");
    }

    @AfterReturning("serviceMethods()")
    public void logAfterMethodExecution() {
        System.out.println("Service method executed successfully.");
    }
}

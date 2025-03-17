package com.example.crudapp.config;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.example.crudapp.service.*.*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBeforeMethodExecution() {
        logger.info("Executing service method...");
    }

    @AfterReturning("serviceMethods()")
    public void logAfterMethodExecution() {
        logger.info("Service method executed successfully.");
    }
}


//@Aspect makes this class an Aspect, meaning it runs additional code before/after method execution.
//@Pointcut("execution(* com.example.crudapp.service.*.*(..))") defines a pattern to match all methods inside service classes.
//@Before logs a message before method execution.
//@AfterReturning logs a message after method execution.

package org.chatting.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(0)
public class ProfileAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileAspect.class);

    @Pointcut("execution(* org.chatting.server.database.DatabaseService.*(..))")
    private void databaseCalls() {
    }

    @Around("databaseCalls()")
    private Object profileDatabaseCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        final long startTime = System.nanoTime();
        final Object object = joinPoint.proceed();
        final long endTime = System.nanoTime();

        final Signature signature = joinPoint.getSignature();
        final double executionTime = (double) (endTime - startTime) / 1000000;
        LOGGER.info(String.format("[PROFILE] Method %s executed in %f ms", signature, executionTime));

        return object;
    }
}

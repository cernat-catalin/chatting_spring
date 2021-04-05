package org.chatting.server.aspect;

import org.aspectj.lang.Signature;

import java.util.logging.Logger;

public aspect ProfileAspect {
    declare precedence:*,ProfileAspect;

    private static final Logger LOGGER = Logger.getLogger("ProfileLogger");


    pointcut databaseCalls():
            call(* org.chatting.server.database.DatabaseService.*(..));

    Object around(): databaseCalls() {
        final long startTime = System.nanoTime();
        final Object object = proceed();
        final long endTime = System.nanoTime();

        final Signature signature = thisJoinPoint.getSignature();
        final double executionTime = (double) (endTime - startTime) / 1000000;
        LOGGER.info(String.format("[PROFILE] Method %s executed in %f ms", signature, executionTime));

        return object;
    }
}

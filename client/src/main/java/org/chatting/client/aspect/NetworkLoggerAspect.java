package org.chatting.client.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class NetworkLoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkLoggerAspect.class);

    @Pointcut("execution(void org.chatting.client.network.NetworkMessageProcessor.processMessage(..))")
    private void networkMessageReceived() {
    }

    @Pointcut("execution(* org.chatting.client.model.NetworkModel.popMessage())")
    private void networkMessageSent() {
    }

    @Before("networkMessageReceived() && args(message)")
    private void logNetworkMessageReceived(Message message) {
        LOGGER.info(String.format("[NETWORK] Message received: %s", message.getMessageType()));
    }

    @AfterReturning(value = "networkMessageSent()", returning = "message")
    private void logNetworkMessageSent(Message message) {
        LOGGER.info(String.format("[NETWORK] Message sent: %s", message.getMessageType()));
    }
}

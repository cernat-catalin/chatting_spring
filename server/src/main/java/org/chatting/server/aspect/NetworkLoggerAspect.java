package org.chatting.server.aspect;

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

    @Pointcut("execution(void org.chatting.server.network.NetworkMessageProcessor.processMessage(..))")
    private void processMessage() {
    }

    @Pointcut("execution(void org.chatting.server.network.UserThread.sendMessage(..))")
    private void messageSent() {
    }

    @Before("processMessage() && args(message)")
    private void logNetworkMessageReceived(Message message) {
        LOGGER.info(String.format("[NETWORK] Message received: [%s]", message.getMessageType()));
    }

    @Before("messageSent() && args(message)")
    private void logNetworkMessageSent(Message message) {
        LOGGER.info(String.format("[NETWORK] Message sent: [%s]", message.getMessageType()));
    }
}

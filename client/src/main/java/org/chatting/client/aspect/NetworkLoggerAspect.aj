package org.chatting.client.aspect;

import org.chatting.common.message.Message;

import java.util.logging.Logger;

public aspect NetworkLoggerAspect {

    private static final Logger LOGGER = Logger.getLogger("NetworkLogger");

    pointcut processMessage():
            execution(void org.chatting.client.network.ReadThread.processMessage(..));

    pointcut messageSent():
            execution(void org.chatting.client.network.WriteThread.sendMessage(..));

    before(Message message): processMessage() && args(message) {
        LOGGER.info(String.format("[NETWORK] Message received: [%s]", message.getMessageType()));
    }

    after(Message message): messageSent() && args(message) {
        LOGGER.info(String.format("[NETWORK] Message sent: [%s]", message.getMessageType()));
    }
}

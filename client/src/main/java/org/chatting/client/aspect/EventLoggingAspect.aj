package org.chatting.client.aspect;

import org.aspectj.lang.Signature;
import org.chatting.client.gui.event.Event;

import java.util.logging.Logger;

public aspect EventLoggingAspect {

    private static final Logger LOGGER = Logger.getLogger("EventLogger");

    pointcut eventPush():
            execution(void org.chatting.client.gui.EventQueue.pushEvent(Event));

    pointcut eventProcess():
            execution(void org.chatting.client.gui.EventProcessor.processEvent(Event));

    before(Event event): eventPush() && args(event) {
        final Signature signature = thisEnclosingJoinPointStaticPart.getSignature();
        LOGGER.info(String.format("[Event] Pushed [%s] from [%s]",
                event.getEventType(),
                signature));
    }

    before(Event event): eventProcess() && args(event) {
        LOGGER.info(String.format("[Event] Processed [%s]", event.getEventType()));
    }
}

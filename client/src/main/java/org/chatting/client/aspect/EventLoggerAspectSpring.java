package org.chatting.client.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.client.gui.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class EventLoggerAspectSpring {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventLoggerAspectSpring.class);

    @Pointcut("execution(void org.chatting.client.gui.EventQueue.pushEvent(..))")
    private void eventPush() {
    }

    @Pointcut("execution(* org.chatting.client.gui.EventQueue.popEvent())")
    private void eventPop() {
    }

    @Before("eventPush() && args(event)")
    private void logEventPush(Event event) {
        LOGGER.info(String.format("[Event] Pushed %s", event.getEventType()));
    }

    @AfterReturning(value = "eventPop()", returning = "event")
    private void logEventPop(Event event) {
        LOGGER.info(String.format("[Event] Processed %s", event.getEventType()));
    }
}

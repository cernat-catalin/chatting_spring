package org.chatting.server.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.server.model.User;
import org.chatting.server.network.UserThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class UserLoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoggerAspect.class);
    private static final Map<Long, LocalDateTime> USER_CONNECTIONS = new HashMap<>();


    @Pointcut("execution(* org.chatting.server.model.ServerModel.addUser(..))")
    private void newUserThread() {
    }

    @Pointcut("execution(* org.chatting.server.model.ServerModel.removeUser(..))")
    private void userRemoved() {
    }

    @After(value = "newUserThread() && args(userThread)")
    private void logUserJoin(UserThread userThread) {
        USER_CONNECTIONS.put(userThread.getId(), LocalDateTime.now());
        LOGGER.info(String.format("[USER] New join. There are %d users now.", userThread.getServer().getUserCount()));
    }


    @After("userRemoved() && args(userThread)")
    private void logUserActivityOnLeave(UserThread userThread) {
        final User user = userThread.getUser();
        final String username = (user == null) ? "Anonymous" : user.getUsername();

        long minutesOnline = -1;
        if (USER_CONNECTIONS.containsKey(userThread.getId())) {
            final LocalDateTime connectTime = USER_CONNECTIONS.get(userThread.getId());
            minutesOnline = ChronoUnit.MINUTES.between(connectTime, LocalDateTime.now());
            USER_CONNECTIONS.remove(userThread.getId());
        }

        LOGGER.info(String.format("[USER] %s left. They've spent %d minutes online.", username, minutesOnline));
    }
}

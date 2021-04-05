package org.chatting.server.aspect;

import org.chatting.server.model.User;
import org.chatting.server.network.NetworkService;
import org.chatting.server.network.UserThread;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public aspect UserLoggerAspect {

    private static final Logger LOGGER = Logger.getLogger("UserLogger");
    private static final Map<Long, LocalDateTime> USER_CONNECTIONS = new HashMap<>();

    pointcut newUserThread():
            call(* org.chatting.server.network.UserThread.start());

    pointcut userRemoved():
            execution(* org.chatting.server.network.NetworkService.removeUser(..));

    after(NetworkService networkService, UserThread userThread) returning: newUserThread()
            && this(networkService)
            && target(userThread) {
        USER_CONNECTIONS.put(userThread.getId(), LocalDateTime.now());
        LOGGER.info(String.format("[USER] New join. There are %d users now.", networkService.getUserCount()));
    }

    after(NetworkService networkService, UserThread userThread) returning: userRemoved()
            && this(networkService)
            && args(userThread) {
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

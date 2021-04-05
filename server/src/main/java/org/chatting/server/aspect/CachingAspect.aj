package org.chatting.server.aspect;

import org.chatting.server.entity.UserStatisticsEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public aspect CachingAspect {

    private static final Logger LOGGER = Logger.getLogger("CachingLogger");

    private static final Map<String, UserStatisticsEntity> userStatisticsCache = new HashMap<>();

    pointcut databaseRetrieval():
            call(* org.chatting.server.database.DatabaseService.getUserStatistics(..));

    pointcut incrementLogins():
            call(* org.chatting.server.database.DatabaseService.incrementUserLogins(..));

    pointcut incrementMessages():
            call(* org.chatting.server.database.DatabaseService.incrementUserMessages(..));

    Optional<UserStatisticsEntity> around(String username): databaseRetrieval() && args(username) {
        if (userStatisticsCache.containsKey(username)) {
            LOGGER.info(String.format("[CACHING] Hit UserStatistics, user %s", username));
            return Optional.of(userStatisticsCache.get(username));
        } else {
            final Optional<UserStatisticsEntity> userStatisticsEntityOpt = proceed(username);
            userStatisticsEntityOpt.ifPresent(usts -> userStatisticsCache.put(username, usts));
            return userStatisticsEntityOpt;
        }
    }

    void around(String username): incrementLogins() && args(username) {
        LOGGER.info(String.format("[CACHING] Update NumberOfLogins, user %s", username));
        if (userStatisticsCache.containsKey(username)) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsCache.get(username);
            userStatisticsEntity.setNumberOfLogins(userStatisticsEntity.getNumberOfLogins() + 1);
        }
        proceed(username);
    }

    void around(String username): incrementMessages() && args(username) {
        LOGGER.info(String.format("[CACHING] Update NumberOfMessages, user %s", username));
        if (userStatisticsCache.containsKey(username)) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsCache.get(username);
            userStatisticsEntity.setNumberOfMessages(userStatisticsEntity.getNumberOfMessages() + 1);
        }
        proceed(username);
    }
}

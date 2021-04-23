package org.chatting.server.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.server.entity.UserStatisticsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class CachingAspect {

    private static final Map<String, UserStatisticsEntity> userStatisticsCache = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CachingAspect.class);

    @Pointcut("execution(* org.chatting.server.database.DatabaseService.getUserStatistics(..))")
    private void userStatistics() {
    }

    @Pointcut("execution(* org.chatting.server.database.DatabaseService.incrementUserLogins(..))")
    private void incrementLogins() {
    }

    @Pointcut("execution(* org.chatting.server.database.DatabaseService.incrementUserMessages(..))")
    private void incrementMessages() {
    }

    @Around("userStatistics() && args(username)")
    private Optional<UserStatisticsEntity> cacheUserStatistics(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        if (userStatisticsCache.containsKey(username)) {
            LOGGER.info(String.format("[CACHING] Hit UserStatistics, user %s", username));
            return Optional.of(userStatisticsCache.get(username));
        } else {
            final Optional<UserStatisticsEntity> userStatisticsEntityOpt =
                    (Optional<UserStatisticsEntity>) joinPoint.proceed(new Object[]{username});
            userStatisticsEntityOpt.ifPresent(usts -> userStatisticsCache.put(username, usts));
            return userStatisticsEntityOpt;
        }
    }

    @Around("incrementLogins() && args(username)")
    private void cacheIncrementLogins(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        LOGGER.info(String.format("[CACHING] Update NumberOfLogins, user %s", username));
        if (userStatisticsCache.containsKey(username)) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsCache.get(username);
            userStatisticsEntity.setNumberOfLogins(userStatisticsEntity.getNumberOfLogins() + 1);
        }
        joinPoint.proceed(new Object[]{username});
    }

    @Around("incrementMessages() && args(username)")
    private void cacheIncrementMessages(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        LOGGER.info(String.format("[CACHING] Update NumberOfMessages, user %s", username));
        if (userStatisticsCache.containsKey(username)) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsCache.get(username);
            userStatisticsEntity.setNumberOfMessages(userStatisticsEntity.getNumberOfMessages() + 1);
        }
        joinPoint.proceed(new Object[]{username});
    }
}

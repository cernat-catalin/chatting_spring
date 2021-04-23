package org.chatting.server.aspect;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.chatting.server.database.DatabaseService;
import org.chatting.server.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class SecurityAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAspect.class);

    @Pointcut("execution(boolean org.chatting.server.database.DatabaseService.areCorrectUserCredentials(..))")
    private void userLogin() {
    }

    @Around("userLogin() && this(databaseService) && args(username, password)")
    private boolean checkCredentials(DatabaseService databaseService,
                                     String username, String password) {
        System.out.printf("SECURITY HELOOOO\n");
        final Optional<UserEntity> userEntity = databaseService.getUserByUsername(username);
        if (userEntity.isPresent() && userEntity.get().getPassword().equals(password)) {
            LOGGER.info(String.format("[SECURITY] User %s has successfully connected", username));
            return true;
        } else {
            LOGGER.info(String.format("[SECURITY] User %s did not connect", username));
            return false;
        }
    }
}

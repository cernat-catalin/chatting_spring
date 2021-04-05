package org.chatting.server.aspect;

import org.chatting.common.message.LoginMessage;
import org.chatting.server.database.DatabaseService;
import org.chatting.server.entity.UserEntity;
import org.chatting.server.network.UserThread;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public aspect SecurityAspect {

    private static final Logger LOGGER = Logger.getLogger("SecurityLogger");

    private final DatabaseService databaseService = new DatabaseService();

    pointcut userLogin():
            execution(void org.chatting.server.network.UserThread.processLogin(LoginMessage));

    void around(UserThread userThread, LoginMessage loginMessage): userLogin()
            && args(loginMessage)
            && target(userThread) {
        final String username = loginMessage.getUsername();
        final String password = loginMessage.getPassword();

        final Optional<UserEntity> userEntity = databaseService.getUserByUsername(username);
        if (userEntity.isPresent() && userEntity.get().getPassword().equals(password)) {
            LOGGER.info(String.format("[SECURITY] User %s has successfully connected", username));
            proceed(userThread, loginMessage);
        } else {
            try {
                LOGGER.info(String.format("[SECURITY] User %s did not connect", username));
                userThread.sendLoginResult(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package org.chatting.server.network;

import org.chatting.common.exception.UnsupportedMessageTypeException;
import org.chatting.common.message.*;
import org.chatting.server.database.DatabaseService;
import org.chatting.server.entity.UserStatisticsEntity;
import org.chatting.server.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class NetworkMessageProcessor {

    private final DatabaseService databaseService;
    private UserThread userThread;

    public NetworkMessageProcessor(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public void setUserThread(UserThread userThread) {
        this.userThread = userThread;
    }

    public void processMessage(Message message) throws IOException {
        switch (message.getMessageType()) {
            case LOGIN:
                processLogin((LoginMessage) message);
                break;
            case SIGN_UP:
                processSignup((SignupMessage) message);
                break;
            case USER_SEND_MESSAGE:
                processUserMessage((UserSendMessage) message);
                break;
            case USER_DISCONNECT:
                userThread.quit();
                break;
            default:
                throw new UnsupportedMessageTypeException(message.getMessageType());
        }
    }

    private void processLogin(LoginMessage loginMessage) throws IOException {
        userThread.setUser(constructUser(loginMessage));

        sendLoginResult(true);
        userThread.getServer().sendConnectedUsersList();

        databaseService.incrementUserLogins(userThread.getUser().getUsername());
        sendUserStatistics();

        final String announcement = String.format("%s has joined the chat!", userThread.getUser().getUsername());
        final ChatMessage chatMessage = new ChatMessage(ChatMessage.AuthorType.SERVER, "Server", announcement);
        userThread.getServer().broadcast(chatMessage);
    }

    private User constructUser(LoginMessage loginMessage) {
        final String username = loginMessage.getUsername();
        final String password = loginMessage.getPassword();
        return new User(username, password);
    }

    private void processUserMessage(UserSendMessage userSendMessage) throws IOException {
        final ChatMessage chatMessage = new ChatMessage(ChatMessage.AuthorType.USER,
                userThread.getUser().getUsername(), userSendMessage.getMessage());

        userThread.getServer().broadcast(chatMessage);
        databaseService.incrementUserMessages(userThread.getUser().getUsername());
        sendUserStatistics();
    }

    private void processSignup(SignupMessage signupMessage) throws IOException {
        final String username = signupMessage.getUsername();
        final String password = signupMessage.getPassword();

        try {
            databaseService.addUser(username, password);
            sendSingUpResult(true);
        } catch (Exception ex) {
            sendSingUpResult(false);
        }
    }

    public void sendLoginResult(boolean result) throws IOException {
        final Message loginResultMessage = new LoginResultMessage(result);
        userThread.sendMessage(loginResultMessage);
    }

    private void sendSingUpResult(boolean result) throws IOException {
        final Message signupResultMessage = new SignupResultMessage(result);
        userThread.sendMessage(signupResultMessage);
    }

    private void sendUserStatistics() throws IOException {
        final Optional<UserStatisticsEntity> userStatisticsOpt = databaseService.getUserStatistics(userThread.getUser()
                .getUsername());
        if (userStatisticsOpt.isPresent()) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsOpt.get();
            final UserStatisticsMessage userStatisticsMessage = new UserStatisticsMessage(
                    userStatisticsEntity.getNumberOfLogins(),
                    userStatisticsEntity.getNumberOfMessages());
            userThread.sendMessage(userStatisticsMessage);
        }
    }
}

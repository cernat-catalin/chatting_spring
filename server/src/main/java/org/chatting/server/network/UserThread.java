package org.chatting.server.network;

import org.chatting.common.exception.InvalidMessageException;
import org.chatting.common.exception.UnsupportedMessageTypeException;
import org.chatting.common.message.*;
import org.chatting.server.NetworkException;
import org.chatting.server.database.DatabaseService;
import org.chatting.server.entity.UserStatisticsEntity;
import org.chatting.server.model.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

public class UserThread extends Thread {
    private final NetworkService server;
    private final DatabaseService databaseService;

    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;
    private boolean shouldQuit = false;

    private User user;

    public UserThread(Socket socket, NetworkService server, DatabaseService databaseService) throws IOException {
        this.server = server;
        this.databaseService = databaseService;
        this.reader = new ObjectInputStream(socket.getInputStream());
        this.writer = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            do {
                final Object obj = reader.readObject();
                if (!(obj instanceof Message)) {
                    throw new InvalidMessageException(obj);
                }
                processMessage((Message) obj);
            } while (!shouldQuit);
        } catch (Exception ex) {
            throw new NetworkException(ex);
        } finally {
            processUserDisconnect();
        }
    }

    public User getUser() {
        return user;
    }

    private void processMessage(Message message) throws IOException {
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
                processUserDisconnect();
                break;
            default:
                throw new UnsupportedMessageTypeException(message.getMessageType());
        }
    }

    private void processLogin(LoginMessage loginMessage) throws IOException {
        this.user = constructUser(loginMessage);

        sendLoginResult(true);
        server.sendConnectedUsersList();

        databaseService.incrementUserLogins(user.getUsername());
        sendUserStatistics();

        final String announcement = String.format("%s has joined the chat!", user.getUsername());
        final ChatMessage chatMessage = new ChatMessage(ChatMessage.AuthorType.SERVER, "Server", announcement);
        server.broadcast(chatMessage);
    }

    private User constructUser(LoginMessage loginMessage) {
        final String username = loginMessage.getUsername();
        final String password = loginMessage.getPassword();
        return new User(username, password);
    }

    private void processUserMessage(UserSendMessage userSendMessage) throws IOException {
        final ChatMessage chatMessage = new ChatMessage(ChatMessage.AuthorType.USER,
                user.getUsername(), userSendMessage.getMessage());

        server.broadcast(chatMessage);
        databaseService.incrementUserMessages(user.getUsername());
        sendUserStatistics();
    }

    private void processUserDisconnect() {
        if (!shouldQuit) {
            shouldQuit = true;
            server.removeUser(this);
        }
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
        sendMessage(loginResultMessage);
    }

    private void sendSingUpResult(boolean result) throws IOException {
        final Message signupResultMessage = new SignupResultMessage(result);
        sendMessage(signupResultMessage);
    }

    private void sendUserStatistics() throws IOException {
        final Optional<UserStatisticsEntity> userStatisticsOpt = databaseService.getUserStatistics(user.getUsername());
        if (userStatisticsOpt.isPresent()) {
            final UserStatisticsEntity userStatisticsEntity = userStatisticsOpt.get();
            final UserStatisticsMessage userStatisticsMessage = new UserStatisticsMessage(
                    userStatisticsEntity.getNumberOfLogins(),
                    userStatisticsEntity.getNumberOfMessages());
            sendMessage(userStatisticsMessage);
        }
    }

    void sendMessage(Message message) throws IOException {
        writer.writeObject(message);
    }
}

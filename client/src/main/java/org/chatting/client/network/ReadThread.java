package org.chatting.client.network;

import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.event.*;
import org.chatting.client.model.NetworkModel;
import org.chatting.common.exception.InvalidMessageException;
import org.chatting.common.exception.UnsupportedMessageTypeException;
import org.chatting.common.message.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ReadThread extends Thread {

    private final NetworkModel networkModel;
    private final EventQueue eventQueue;
    private final ObjectInputStream reader;

    public ReadThread(Socket socket, NetworkModel networkModel, EventQueue eventQueue) throws IOException {
        this.networkModel = networkModel;
        this.eventQueue = eventQueue;
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (!networkModel.shouldQuit()) {
                final Object obj = reader.readObject();
                if (!(obj instanceof Message)) {
                    throw new InvalidMessageException(obj);
                }
                processMessage((Message) obj);
            }
        } catch (SocketException ignored) {
            if (!networkModel.shouldQuit()) {
                throw new RuntimeException("Socket exception before client close");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.printf("ERROR while closing reader stream %s\n", e);
            }
        }
    }

    private void processMessage(Message message) {
        switch (message.getMessageType()) {
            case CHAT_MESSAGE:
                final ChatMessage chatMessage = (ChatMessage) message;
                final Event chatMessageReceived = new ChatMessageReceivedEvent(
                        ChatMessageReceivedEvent.AuthorType.valueOf(chatMessage.getAuthorType().name()),
                        chatMessage.getAuthorName(),
                        chatMessage.getMessage()
                );
                eventQueue.pushEvent(chatMessageReceived);
                break;
            case LOGIN_RESULT:
                final LoginResultMessage loginResultMessage = (LoginResultMessage) message;
                final Event loginResultEvent = new LoginResultEvent(loginResultMessage.isLoginAccepted());
                eventQueue.pushEvent(loginResultEvent);
                break;
            case SIGN_UP_RESULT:
                final SignupResultMessage signupResultMessage = (SignupResultMessage) message;
                final Event signupResultEvent = new SignupResultEvent(signupResultMessage.isSignupResult());
                eventQueue.pushEvent(signupResultEvent);
                break;
            case USER_LIST:
                final UserListMessage userListMessage = (UserListMessage) message;
                final Event userListReceived = new UserListReceivedEvent(userListMessage.getConnectedUsers());
                eventQueue.pushEvent(userListReceived);
                break;
            case USER_STATISTICS:
                final UserStatisticsMessage userStatisticsMessage = (UserStatisticsMessage) message;
                final Event userStatisticsReceivedEvent = new UserStatisticsReceivedEvent(
                        userStatisticsMessage.getNumberOfLogins(),
                        userStatisticsMessage.getNumberOfMessages());
                eventQueue.pushEvent(userStatisticsReceivedEvent);
                break;
            default:
                throw new UnsupportedMessageTypeException(message.getMessageType());
        }
    }
}
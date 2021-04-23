package org.chatting.client.network;

import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.event.*;
import org.chatting.common.exception.UnsupportedMessageTypeException;
import org.chatting.common.message.*;

public class NetworkMessageProcessor {

    private final EventQueue eventQueue;

    public NetworkMessageProcessor(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    public void processMessage(Message message) {
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
                final Event signupResultEvent = new SignupResultEvent(signupResultMessage.isSignupResult(), signupResultMessage.getReason());
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

package org.chatting.common.message;

import java.io.Serializable;

public class UserSendMessage implements Message, Serializable {

    private String message;

    public UserSendMessage() {

    }

    public UserSendMessage(String message) {
        this.message = message;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_SEND_MESSAGE;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

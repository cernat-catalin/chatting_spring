package org.chatting.common.message;

import java.io.Serializable;

public class UserDisconnectedMessage implements Message, Serializable {

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_DISCONNECT;
    }
}

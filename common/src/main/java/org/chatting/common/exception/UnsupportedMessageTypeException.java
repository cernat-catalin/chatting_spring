package org.chatting.common.exception;

import org.chatting.common.message.MessageType;

public class UnsupportedMessageTypeException extends RuntimeException {

    private final MessageType messageType;

    public UnsupportedMessageTypeException(MessageType messageType) {
        super(String.format("Unsupported message type: %s", messageType));
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}

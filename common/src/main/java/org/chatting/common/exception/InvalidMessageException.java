package org.chatting.common.exception;

public class InvalidMessageException extends RuntimeException {
    private final Object object;

    public InvalidMessageException(Object object) {
        super(String.format("Expected a network message. Got: %s instead.", object));
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}

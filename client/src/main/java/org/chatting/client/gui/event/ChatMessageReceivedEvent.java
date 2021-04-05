package org.chatting.client.gui.event;

import java.io.Serializable;

public class ChatMessageReceivedEvent implements Event {

    private final AuthorType authorType;
    private final String authorName;
    private final String message;

    public ChatMessageReceivedEvent(AuthorType authorType, String authorName, String message) {
        this.authorType = authorType;
        this.authorName = authorName;
        this.message = message;
    }

    @Override
    public EventType getEventType() {
        return EventType.CHAT_MESSAGE_RECEIVED;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getMessage() {
        return message;
    }

    public enum AuthorType implements Serializable {
        USER, SERVER
    }
}

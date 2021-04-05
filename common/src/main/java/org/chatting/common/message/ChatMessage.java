package org.chatting.common.message;

import java.io.Serializable;

public class ChatMessage implements Message, Serializable {

    private AuthorType authorType;
    private String authorName;
    private String message;

    public ChatMessage() {

    }

    public ChatMessage(AuthorType authorType, String authorName, String message) {
        this.authorType = authorType;
        this.authorName = authorName;
        this.message = message;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT_MESSAGE;
    }

    public AuthorType getAuthorType() {
        return authorType;
    }

    public void setAuthorType(AuthorType authorType) {
        this.authorType = authorType;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public enum AuthorType implements Serializable {
        USER, SERVER
    }
}

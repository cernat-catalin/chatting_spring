package org.chatting.common.message;

import java.io.Serializable;

public class SignupMessage implements Message, Serializable {

    private String username;
    private String password;

    public SignupMessage() {

    }

    public SignupMessage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SIGN_UP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

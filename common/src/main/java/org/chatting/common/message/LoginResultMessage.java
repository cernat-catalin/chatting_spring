package org.chatting.common.message;

import java.io.Serializable;

public class LoginResultMessage implements Message, Serializable {

    private boolean loginAccepted;

    public LoginResultMessage() {

    }

    public LoginResultMessage(boolean loginAccepted) {
        this.loginAccepted = loginAccepted;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_RESULT;
    }

    public boolean isLoginAccepted() {
        return loginAccepted;
    }

    public void setLoginAccepted(boolean loginAccepted) {
        this.loginAccepted = loginAccepted;
    }
}

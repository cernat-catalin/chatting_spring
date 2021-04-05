package org.chatting.common.message;

import java.io.Serializable;

public class SignupResultMessage implements Message, Serializable {

    private boolean signupResult;
    private String reason;

    public SignupResultMessage() {

    }

    public SignupResultMessage(boolean signupResult) {
        this.signupResult = signupResult;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SIGN_UP_RESULT;
    }

    public boolean isSignupResult() {
        return signupResult;
    }

    public void setSignupResult(boolean signupResult) {
        this.signupResult = signupResult;
    }
}

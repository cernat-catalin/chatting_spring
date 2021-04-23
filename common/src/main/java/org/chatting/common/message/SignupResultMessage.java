package org.chatting.common.message;

import java.io.Serializable;

public class SignupResultMessage implements Message, Serializable {

    private boolean signupResult;
    private String reason;

    public SignupResultMessage() {

    }

    public SignupResultMessage(boolean signupResult, String reason) {
        this.signupResult = signupResult;
        this.reason = reason;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

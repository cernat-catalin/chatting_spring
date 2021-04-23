package org.chatting.client.gui.event;

public class SignupResultEvent implements Event {

    public final String reason;
    private final boolean signupAccepted;

    public SignupResultEvent(boolean signupAccepted, String reason) {
        this.signupAccepted = signupAccepted;
        this.reason = reason;
    }

    @Override
    public EventType getEventType() {
        return EventType.SIGN_UP_RESULT_RECEIVED;
    }

    public boolean isSignupAccepted() {
        return signupAccepted;
    }

    public String getReason() {
        return reason;
    }
}

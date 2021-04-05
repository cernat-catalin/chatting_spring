package org.chatting.client.gui.event;

public class LoginResultEvent implements Event {

    private final boolean loginAccepted;

    public LoginResultEvent(boolean loginAccepted) {
        this.loginAccepted = loginAccepted;
    }

    @Override
    public EventType getEventType() {
        return EventType.LOGIN_RESULT_RECEIVED;
    }

    public boolean isLoginAccepted() {
        return loginAccepted;
    }
}

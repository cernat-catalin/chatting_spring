package org.chatting.client.gui.event;

public class LoginButtonClickEvent implements Event {

    private final String username;
    private final String password;

    public LoginButtonClickEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public EventType getEventType() {
        return EventType.LOGIN_BUTTON_CLICK;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

package org.chatting.client.gui.event;

public class SendButtonClickEvent implements Event {

    private final String textFieldText;

    public SendButtonClickEvent(String textFieldText) {
        this.textFieldText = textFieldText;
    }

    @Override
    public EventType getEventType() {
        return EventType.SEND_BUTTON_CLICK;
    }

    public String getTextFieldText() {
        return textFieldText;
    }
}

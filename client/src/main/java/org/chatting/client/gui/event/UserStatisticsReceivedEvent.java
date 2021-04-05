package org.chatting.client.gui.event;

public class UserStatisticsReceivedEvent implements Event {

    private final int numberOfLogins;
    private final int numberOfMessages;

    public UserStatisticsReceivedEvent(int numberOfLogins, int numberOfMessages) {
        this.numberOfLogins = numberOfLogins;
        this.numberOfMessages = numberOfMessages;
    }

    @Override
    public EventType getEventType() {
        return EventType.USER_STATISTICS_RECEIVED;
    }

    public int getNumberOfLogins() {
        return numberOfLogins;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }
}

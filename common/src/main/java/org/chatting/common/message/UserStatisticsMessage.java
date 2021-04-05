package org.chatting.common.message;

import java.io.Serializable;

public class UserStatisticsMessage implements Message, Serializable {

    private int numberOfLogins;
    private int numberOfMessages;

    public UserStatisticsMessage() {

    }

    public UserStatisticsMessage(int numberOfLogins, int numberOfMessages) {
        this.numberOfLogins = numberOfLogins;
        this.numberOfMessages = numberOfMessages;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_STATISTICS;
    }

    public int getNumberOfLogins() {
        return numberOfLogins;
    }

    public void setNumberOfLogins(int numberOfLogins) {
        this.numberOfLogins = numberOfLogins;
    }

    public int getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }
}

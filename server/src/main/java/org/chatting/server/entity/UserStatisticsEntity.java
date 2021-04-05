package org.chatting.server.entity;

public class UserStatisticsEntity {

    private int numberOfLogins;
    private int numberOfMessages;

    public UserStatisticsEntity() {

    }

    public UserStatisticsEntity(int numberOfLogins, int numberOfMessages) {
        this.numberOfLogins = numberOfLogins;
        this.numberOfMessages = numberOfMessages;
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

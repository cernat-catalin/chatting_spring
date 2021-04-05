package org.chatting.common.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserListMessage implements Message, Serializable {

    private List<String> connectedUsers = new ArrayList<>();

    public UserListMessage() {

    }

    public UserListMessage(List<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.USER_LIST;
    }

    public List<String> getConnectedUsers() {
        return connectedUsers;
    }

    public void setConnectedUsers(List<String> connectedUsers) {
        this.connectedUsers = connectedUsers;
    }
}

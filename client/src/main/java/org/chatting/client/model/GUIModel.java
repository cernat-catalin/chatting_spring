package org.chatting.client.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.List;

public class GUIModel {

    private final StringProperty loginError = new SimpleStringProperty();
    private final StringProperty signupError = new SimpleStringProperty();
    private final StringProperty numberOfLogins = new SimpleStringProperty("Logins: 0");
    private final StringProperty numberOfMessages = new SimpleStringProperty("Messages: 0");
    private ListView<String> chatMessagesListView;
    private ListView<String> connectedUsersListView;

    public void setChatMessagesListView(ListView<String> chatMessagesListView) {
        this.chatMessagesListView = chatMessagesListView;
    }

    public void addChatMessage(String chatMessage) {
        chatMessagesListView.getItems().add(chatMessage);
        chatMessagesListView.scrollTo(chatMessagesListView.getItems().size() - 1);
    }

    public void setConnectedUsersListView(ListView<String> connectedUsersListView) {
        this.connectedUsersListView = connectedUsersListView;
    }

    public void setConnectedUsers(List<String> connectedUsers) {
        connectedUsersListView.setItems(FXCollections.observableArrayList(connectedUsers));
    }

    public StringProperty getLoginError() {
        return loginError;
    }

    public void setLoginError() {
        loginError.set("Invalid credentials");
    }

    public void clearLoginError() {
        loginError.set("");
    }

    public StringProperty getSignupError() {
        return signupError;
    }

    public void setSignupError(String reason) {
        signupError.set(reason);
    }

    public void clearSignupError() {
        signupError.set("");
    }

    public StringProperty getNumberOfLogins() {
        return numberOfLogins;
    }

    public void setNumberOfLogins(int n) {
        numberOfLogins.set(String.format("Logins: %d", n));
    }

    public StringProperty getNumberOfMessages() {
        return numberOfMessages;
    }

    public void setNumberOfMessages(int n) {
        numberOfMessages.set(String.format("Messages: %d", n));
    }
}

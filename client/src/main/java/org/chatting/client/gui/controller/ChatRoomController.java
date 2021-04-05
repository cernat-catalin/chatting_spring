package org.chatting.client.gui.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.event.Event;
import org.chatting.client.gui.event.SendButtonClickEvent;
import org.chatting.client.model.GUIModel;

public class ChatRoomController {

    private final GUIModel guiModel;
    private final EventQueue eventQueue;
    private final Scene scene;

    public ChatRoomController(GUIModel guiModel, EventQueue eventQueue) {
        this.guiModel = guiModel;
        this.eventQueue = eventQueue;
        this.scene = generateChatRoomScene();
    }

    public Scene getScene() {
        return scene;
    }

    private Scene generateChatRoomScene() {
        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));


        // Connected Users
        final Label usersTitleLabel = new Label();
        usersTitleLabel.setText("Connected users");

        final ListView<String> usersListView = new ListView<>();
        usersListView.setMinHeight(495);
        usersListView.setMinWidth(130);
        usersListView.setMaxWidth(120);
        guiModel.setConnectedUsersListView(usersListView);

        // User Statistics
        final Text numberOfLogins = new Text();
        numberOfLogins.textProperty().bindBidirectional(guiModel.getNumberOfLogins());
        final Text numberOfMessages = new Text();
        numberOfMessages.textProperty().bindBidirectional(guiModel.getNumberOfMessages());

        final VBox usersBox = new VBox(5);
        usersBox.getChildren().add(usersTitleLabel);
        usersBox.getChildren().add(usersListView);
        usersBox.getChildren().add(numberOfLogins);
        usersBox.getChildren().add(numberOfMessages);
        grid.add(usersBox, 0, 0);

        // Chat
        final ListView<String> chatListView = new ListView<>();
        chatListView.setMinWidth(1000);
        chatListView.setMaxWidth(1000);
        chatListView.setMinHeight(510);
        guiModel.setChatMessagesListView(chatListView);

        final HBox chatBox = new HBox();
        chatBox.getChildren().add(chatListView);
        grid.add(chatBox, 1, 0);

        // Send text area
        final HBox sendTextBox = new HBox(10);
        final TextField sendTextField = new TextField();
        final Button sendBtn = new Button("Send");

        sendTextBox.getChildren().add(sendTextField);
        sendTextBox.getChildren().add(sendBtn);
        HBox.setHgrow(sendTextField, Priority.ALWAYS);
        grid.add(sendTextBox, 1, 1);

        sendBtn.setOnAction(e -> {
            final String text = sendTextField.getText();
            final Event sendButtonClick = new SendButtonClickEvent(text);
            eventQueue.pushEvent(sendButtonClick);
            sendTextField.clear();
        });

        sendTextField.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                final String text = sendTextField.getText();
                final Event sendButtonClick = new SendButtonClickEvent(text);
                eventQueue.pushEvent(sendButtonClick);
                sendTextField.clear();
            }
        });

        return new Scene(grid, 1200, 650);
    }
}

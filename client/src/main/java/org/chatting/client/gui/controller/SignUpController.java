package org.chatting.client.gui.controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.event.ChangeSceneEvent;
import org.chatting.client.gui.event.Event;
import org.chatting.client.gui.event.SignUpButtonClickEvent;
import org.chatting.client.model.GUIModel;

public class SignUpController {

    private final EventQueue eventQueue;
    private final GUIModel guiModel;
    private final Scene scene;

    public SignUpController(GUIModel guiModel, EventQueue eventQueue) {
        this.guiModel = guiModel;
        this.eventQueue = eventQueue;
        this.scene = generateSignUpScene();
    }

    public Scene getScene() {
        return scene;
    }

    private Scene generateSignUpScene() {
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Title
        final Text sceneTitle = new Text("Create account");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        // Username Field
        final Label usernameLabel = new Label("User Name:");
        grid.add(usernameLabel, 0, 1);
        final TextField usernameFiled = new TextField();
        grid.add(usernameFiled, 1, 1);

        // Password Field
        final Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        final PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        // Create Account Button
        final Button createAccountButton = new Button("Create Account");
        final Button backButton = new Button("Back");
        final Text signupError = new Text();
        signupError.textProperty().bindBidirectional(guiModel.getSignupError());

        final HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(signupError);
        hbBtn.getChildren().add(createAccountButton);
        hbBtn.getChildren().add(backButton);
        grid.add(hbBtn, 1, 4);

        backButton.setOnAction(e -> {
            final Event changeSceneEvent = new ChangeSceneEvent(SceneType.LOGIN);
            eventQueue.pushEvent(changeSceneEvent);
        });

        createAccountButton.setOnAction(e -> {
            final String username = usernameFiled.getText();
            final String password = passwordField.getText();
            final Event signUpButtonClickEvent = new SignUpButtonClickEvent(username, password);
            eventQueue.pushEvent(signUpButtonClickEvent);
        });

        return new Scene(grid, 500, 200);
    }
}

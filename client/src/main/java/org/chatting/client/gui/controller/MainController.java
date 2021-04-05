package org.chatting.client.gui.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.chatting.client.gui.EventQueue;
import org.chatting.client.model.GUIModel;

public class MainController {
    private final LoginController loginController;
    private final ChatRoomController chatRoomController;
    private final SignUpController signUpController;
    private final GUIModel guiModel;
    private Scene currentScene;

    public MainController(EventQueue eventQueue) {
        this.guiModel = new GUIModel();
        this.loginController = new LoginController(guiModel, eventQueue);
        this.chatRoomController = new ChatRoomController(guiModel, eventQueue);
        this.signUpController = new SignUpController(guiModel, eventQueue);
        this.currentScene = loginController.getScene();
    }

    public GUIModel getGuiModel() {
        return guiModel;
    }

    public void changeScene(SceneType sceneType) {
        switch (sceneType) {
            case LOGIN:
                changeCurrentScene(loginController.getScene());
                break;
            case CHAT_ROOM:
                changeCurrentScene(chatRoomController.getScene());
                break;
            case SIGN_UP:
                changeCurrentScene(signUpController.getScene());
                break;
        }
    }

    private void changeCurrentScene(Scene scene) {
        currentScene = scene;
        currentStage().setScene(currentScene);
    }

    public Stage currentStage() {
        return (Stage) Stage.getWindows().filtered(Window::isShowing).get(0);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}

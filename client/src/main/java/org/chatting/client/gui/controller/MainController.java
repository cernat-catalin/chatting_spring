package org.chatting.client.gui.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.chatting.client.model.GUIModel;

public class MainController {

    private final GUIModel guiModel;
    private final LoginController loginController;
    private final ChatRoomController chatRoomController;
    private final SignUpController signUpController;

    private Scene currentScene;

    public MainController(GUIModel guiModel,
                          LoginController loginController,
                          ChatRoomController chatRoomController,
                          SignUpController signUpController) {
        this.guiModel = guiModel;
        this.loginController = loginController;
        this.chatRoomController = chatRoomController;
        this.signUpController = signUpController;
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

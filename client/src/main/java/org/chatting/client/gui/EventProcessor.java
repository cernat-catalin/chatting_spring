package org.chatting.client.gui;

import javafx.application.Platform;
import org.chatting.client.gui.controller.MainController;
import org.chatting.client.gui.controller.SceneType;
import org.chatting.client.gui.event.*;
import org.chatting.client.network.NetworkService;
import org.chatting.common.message.LoginMessage;
import org.chatting.common.message.Message;
import org.chatting.common.message.SignupMessage;
import org.chatting.common.message.UserSendMessage;

public class EventProcessor extends Thread {

    private static final int SLEEP_BETWEEN_READ_CHECKS = 100;

    private final EventQueue eventQueue;
    private final MainController mainController;
    private final NetworkService networkService;
    private boolean shouldQuit = false;

    public EventProcessor(EventQueue eventQueue, MainController mainController, NetworkService networkService) {
        this.eventQueue = eventQueue;
        this.mainController = mainController;
        this.networkService = networkService;
    }

    public void run() {
        try {
            while (!shouldQuit) {
                while (eventQueue.size() > 0) {
                    final Event event = eventQueue.popEvent();
                    processEvent(event);
                }
                Thread.sleep(SLEEP_BETWEEN_READ_CHECKS);
            }
        } catch (InterruptedException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void processEvent(Event event) {
        switch (event.getEventType()) {
            case LOGIN_BUTTON_CLICK:
                final LoginButtonClickEvent loginButtonClickEvent = (LoginButtonClickEvent) event;
                final Message loginMessage = new LoginMessage(loginButtonClickEvent.getUsername(), loginButtonClickEvent.getPassword());
                networkService.sendMessage(loginMessage);
                break;
            case LOGIN_RESULT_RECEIVED:
                final LoginResultEvent loginResultEvent = (LoginResultEvent) event;
                Platform.runLater(() -> {
                    if (loginResultEvent.isLoginAccepted()) {
                        mainController.getGuiModel().clearLoginError();
                        mainController.changeScene(SceneType.CHAT_ROOM);
                    } else {
                        mainController.getGuiModel().setLoginError();
                    }
                });
                break;
            case SIGN_UP_RESULT_RECEIVED:
                final SignupResultEvent signupResultEvent = (SignupResultEvent) event;
                final String reason = signupResultEvent.getReason();
                Platform.runLater(() -> {
                    if (signupResultEvent.isSignupAccepted()) {
                        mainController.getGuiModel().clearSignupError();
                        mainController.getGuiModel().clearLoginError();
                        mainController.changeScene(SceneType.LOGIN);
                    } else {
                        mainController.getGuiModel().setSignupError(reason);
                    }
                });
                break;
            case SEND_BUTTON_CLICK:
                final SendButtonClickEvent sendButtonClickEvent = (SendButtonClickEvent) event;
                final String message = sendButtonClickEvent.getTextFieldText();
                final Message userChatMessage = new UserSendMessage(message);
                networkService.sendMessage(userChatMessage);
                break;
            case CHAT_MESSAGE_RECEIVED:
                final ChatMessageReceivedEvent chatMessageReceivedEvent = (ChatMessageReceivedEvent) event;
                Platform.runLater(() -> mainController.getGuiModel().addChatMessage(formatMessage(chatMessageReceivedEvent)));
                break;
            case USER_LIST_RECEIVED:
                final UserListReceivedEvent userListReceivedEvent = (UserListReceivedEvent) event;
                Platform.runLater(() -> mainController.getGuiModel().setConnectedUsers(userListReceivedEvent.getConnectedUsers()));
                break;
            case CHANGE_SCENE:
                final ChangeSceneEvent changeSceneEvent = (ChangeSceneEvent) event;
                Platform.runLater(() -> mainController.changeScene(changeSceneEvent.getSceneType()));
                break;
            case SIGN_UP_BUTTON_CLICK:
                final SignUpButtonClickEvent signUpButtonClickEvent = (SignUpButtonClickEvent) event;
                final Message signupMessage = new SignupMessage(signUpButtonClickEvent.getUsername(), signUpButtonClickEvent.getPassword());
                networkService.sendMessage(signupMessage);
                break;
            case USER_STATISTICS_RECEIVED:
                final UserStatisticsReceivedEvent userStatisticsReceivedEvent = (UserStatisticsReceivedEvent) event;
                Platform.runLater(() -> {
                    mainController.getGuiModel().setNumberOfLogins(userStatisticsReceivedEvent.getNumberOfLogins());
                    mainController.getGuiModel().setNumberOfMessages(userStatisticsReceivedEvent.getNumberOfMessages());
                });
                break;
            default:
                throw new RuntimeException("Unsupported message type in processing loop. Message Type: " + event.getEventType());
        }
    }

    public void stopProcessing() {
        shouldQuit = true;
    }

    private String formatMessage(ChatMessageReceivedEvent chatMessageReceivedEvent) {
        switch (chatMessageReceivedEvent.getAuthorType()) {
            case SERVER:
                return String.format("[SERVER]: %s", chatMessageReceivedEvent.getMessage());
            case USER:
                return String.format("%s: %s", chatMessageReceivedEvent.getAuthorName(), chatMessageReceivedEvent.getMessage());
            default:
                return chatMessageReceivedEvent.getMessage();
        }
    }
}

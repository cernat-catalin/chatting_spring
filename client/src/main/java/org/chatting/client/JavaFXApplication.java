package org.chatting.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.chatting.client.gui.EventProcessor;
import org.chatting.client.gui.controller.MainController;
import org.chatting.client.network.NetworkService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXApplication extends Application {

    private ConfigurableApplicationContext springContext;

    private NetworkService networkService;
    private MainController mainController;
    private EventProcessor eventProcessor;

    @Override
    public void init() {
        final String[] args = getParameters().getRaw().toArray(new String[0]);
        springContext = new SpringApplicationBuilder()
                .sources(SpringBootMain.class)
                .run(args);

        this.networkService = springContext.getBean(NetworkService.class);
        this.mainController = springContext.getBean(MainController.class);
        this.eventProcessor = springContext.getBean(EventProcessor.class);
    }

    @Override
    public void start(Stage stage) {
        networkService.start();
        eventProcessor.start();

        final Scene loginScene = mainController.getCurrentScene();
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() {
        networkService.stopProcessing();
        eventProcessor.stopProcessing();
        springContext.close();
    }
}

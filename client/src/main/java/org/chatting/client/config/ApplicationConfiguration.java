package org.chatting.client.config;

import org.chatting.client.gui.EventProcessor;
import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.controller.ChatRoomController;
import org.chatting.client.gui.controller.LoginController;
import org.chatting.client.gui.controller.MainController;
import org.chatting.client.gui.controller.SignUpController;
import org.chatting.client.model.GUIModel;
import org.chatting.client.model.NetworkModel;
import org.chatting.client.network.NetworkMessageProcessor;
import org.chatting.client.network.NetworkService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties(ApplicationProperties.class)
public class ApplicationConfiguration {

    private final ApplicationProperties properties;

    public ApplicationConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public EventQueue eventQueue() {
        return new EventQueue();
    }

    @Bean
    public NetworkModel networkModel() {
        return new NetworkModel();
    }

    @Bean
    public NetworkMessageProcessor networkMessageProcessor() {
        return new NetworkMessageProcessor(eventQueue());
    }

    @Bean
    public NetworkService networkService() {
        final String hostname = properties.getNetwork().getHostname();
        final int port = properties.getNetwork().getPort();
        return new NetworkService(hostname, port, networkMessageProcessor(), networkModel());
    }

    // GUI

    @Bean
    public EventProcessor eventProcessor() {
        return new EventProcessor(eventQueue(), mainController(), networkService());
    }

    @Bean
    public GUIModel guiModel() {
        return new GUIModel();
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(guiModel(), eventQueue());
    }

    @Bean
    public ChatRoomController chatRoomController() {
        return new ChatRoomController(guiModel(), eventQueue());
    }

    @Bean
    public SignUpController signUpController() {
        return new SignUpController(guiModel(), eventQueue());
    }

    @Bean
    public MainController mainController() {
        return new MainController(guiModel(), loginController(), chatRoomController(), signUpController());
    }
}

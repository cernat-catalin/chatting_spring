package org.chatting.client.config;

import org.chatting.client.gui.EventProcessor;
import org.chatting.client.gui.EventQueue;
import org.chatting.client.gui.controller.MainController;
import org.chatting.client.network.NetworkService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
    public NetworkService networkService() {
        final String hostname = properties.getNetwork().getHostname();
        final int port = properties.getNetwork().getPort();
        return new NetworkService(hostname, port, eventQueue());
    }

    @Bean
    public MainController mainController() {
        return new MainController(eventQueue());
    }

    @Bean
    public EventProcessor eventProcessor() {
        return new EventProcessor(eventQueue(), mainController(), networkService());
    }
}

package org.chatting.server;

import org.chatting.server.network.NetworkService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerRunner implements CommandLineRunner {

    private final NetworkService networkService;

    public ServerRunner(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public void run(String[] args) {
        networkService.start();
    }
}

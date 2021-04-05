package org.chatting.server;

import org.chatting.server.database.DatabaseService;
import org.chatting.server.network.NetworkService;

public class Main {

    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%1$tF %1$tT [%4$s] %5$s%6$s%n");
        final int port = 8000;
        final DatabaseService databaseService = new DatabaseService();
        final NetworkService networkService = new NetworkService(port, databaseService);
        networkService.start();
    }
}

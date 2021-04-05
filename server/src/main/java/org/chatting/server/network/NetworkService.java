package org.chatting.server.network;

import org.chatting.common.message.Message;
import org.chatting.common.message.UserListMessage;
import org.chatting.server.NetworkException;
import org.chatting.server.database.DatabaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NetworkService {
    private final int port;
    private final Set<UserThread> userThreads = new HashSet<>();
    private final DatabaseService databaseService;

    public NetworkService(int port, DatabaseService databaseService) {
        this.port = port;
        this.databaseService = databaseService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                final Socket socket = serverSocket.accept();
                final UserThread newUser = new UserThread(socket, this, databaseService);
                userThreads.add(newUser);
                newUser.start();
            }

        } catch (IOException ex) {
            throw new NetworkException(ex);
        }
    }

    public int getUserCount() {
        return userThreads.size();
    }

    void broadcast(Message message) throws IOException {
        for (UserThread userThread : userThreads) {
            userThread.sendMessage(message);
        }
    }

    void removeUser(UserThread userThread) {
        try {
            userThreads.remove(userThread);
            sendConnectedUsersList();
        } catch (IOException ex) {
            System.out.printf("Error removing user %s\n", ex);
        }
    }

    void sendConnectedUsersList() throws IOException {
        final List<String> connectedUsers = userThreads.stream()
                .filter(ut -> ut.getUser() != null)
                .map(ut -> ut.getUser().getUsername())
                .collect(Collectors.toList());

        final Message message = new UserListMessage(connectedUsers);
        for (UserThread userThread : userThreads) {
            userThread.sendMessage(message);
        }
    }
}

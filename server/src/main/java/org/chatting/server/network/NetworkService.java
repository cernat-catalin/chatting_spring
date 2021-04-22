package org.chatting.server.network;

import org.chatting.common.message.Message;
import org.chatting.common.message.UserListMessage;
import org.chatting.server.exception.NetworkException;
import org.chatting.server.model.ServerModel;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkService {
    private final int port;
    private final ServerModel serverModel;
    private final ApplicationContext applicationContext;

    public NetworkService(ApplicationContext applicationContext, int port, ServerModel serverModel) {
        this.applicationContext = applicationContext;
        this.port = port;
        this.serverModel = serverModel;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                final Socket socket = serverSocket.accept();
                final UserThread userThread = applicationContext.getBean(UserThread.class);
                final NetworkMessageProcessor messageProcessor = applicationContext.getBean(NetworkMessageProcessor.class);

                userThread.init(socket, messageProcessor);
                messageProcessor.setUserThread(userThread);
                addUser(userThread);
            }

        } catch (IOException ex) {
            throw new NetworkException(ex);
        }
    }

    public int getUserCount() {
        return serverModel.getUserThreads().size();
    }

    void broadcast(Message message) throws IOException {
        for (UserThread userThread : serverModel.getUserThreads()) {
            userThread.sendMessage(message);
        }
    }

    void removeUser(UserThread userThread) {
        try {
            serverModel.removeUser(userThread);
            sendConnectedUsersList();
        } catch (IOException ex) {
            System.out.printf("Error removing user %s\n", ex);
        }
    }

    private void addUser(UserThread userThread) throws IOException {
        serverModel.addUser(userThread);
        userThread.start();
    }

    void sendConnectedUsersList() throws IOException {
        final List<String> connectedUsers = serverModel.getUserThreads().stream()
                .filter(ut -> ut.getUser() != null)
                .map(ut -> ut.getUser().getUsername())
                .collect(Collectors.toList());

        final Message message = new UserListMessage(connectedUsers);
        for (UserThread userThread : serverModel.getUserThreads()) {
            userThread.sendMessage(message);
        }
    }
}

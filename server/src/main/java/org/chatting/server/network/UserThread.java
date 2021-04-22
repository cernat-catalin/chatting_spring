package org.chatting.server.network;

import org.chatting.common.exception.InvalidMessageException;
import org.chatting.common.message.Message;
import org.chatting.server.exception.NetworkException;
import org.chatting.server.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class UserThread extends Thread {
    private final NetworkService server;

    private NetworkMessageProcessor networkMessageProcessor;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private boolean shouldQuit = false;

    private User user;

    public UserThread(NetworkService server) {
        this.server = server;
    }

    public void init(Socket socket, NetworkMessageProcessor networkMessageProcessor) throws IOException {
        this.reader = new ObjectInputStream(socket.getInputStream());
        this.writer = new ObjectOutputStream(socket.getOutputStream());
        this.networkMessageProcessor = networkMessageProcessor;
    }

    @Override
    public void run() {
        try {
            do {
                final Object obj = reader.readObject();
                if (!(obj instanceof Message)) {
                    throw new InvalidMessageException(obj);
                }
                networkMessageProcessor.processMessage((Message) obj);
            } while (!shouldQuit);
        } catch (Exception ex) {
            throw new NetworkException(ex);
        } finally {
            quit();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void quit() {
        if (!shouldQuit) {
            shouldQuit = true;
            server.removeUser(this);
        }
    }

    public void sendMessage(Message message) throws IOException {
        writer.writeObject(message);
    }

    public NetworkService getServer() {
        return server;
    }
}

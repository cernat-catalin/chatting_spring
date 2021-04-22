package org.chatting.client.network;

import org.chatting.client.model.NetworkModel;
import org.chatting.common.exception.InvalidMessageException;
import org.chatting.common.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class ReadThread extends Thread {

    private final NetworkModel networkModel;
    private final NetworkMessageProcessor networkMessageProcessor;
    private final ObjectInputStream reader;

    public ReadThread(Socket socket, NetworkModel networkModel, NetworkMessageProcessor networkMessageProcessor) throws IOException {
        this.networkModel = networkModel;
        this.networkMessageProcessor = networkMessageProcessor;
        this.reader = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (!networkModel.shouldQuit()) {
                final Object obj = reader.readObject();
                if (!(obj instanceof Message)) {
                    throw new InvalidMessageException(obj);
                }
                networkMessageProcessor.processMessage((Message) obj);
            }
        } catch (SocketException ignored) {
            if (!networkModel.shouldQuit()) {
                throw new RuntimeException("Socket exception before client close");
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.printf("ERROR while closing reader stream %s\n", e);
            }
        }
    }
}
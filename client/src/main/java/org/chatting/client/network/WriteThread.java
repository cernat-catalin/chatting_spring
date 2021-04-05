package org.chatting.client.network;

import org.chatting.client.model.NetworkModel;
import org.chatting.common.message.Message;
import org.chatting.common.message.UserDisconnectedMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WriteThread extends Thread {

    private static final int SLEEP_BETWEEN_EVENT_CHECKS = 100;

    private final NetworkModel networkModel;
    private final ObjectOutputStream writer;

    public WriteThread(Socket socket, NetworkModel networkModel) throws IOException {
        this.networkModel = networkModel;
        this.writer = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (!networkModel.shouldQuit()) {
                while (networkModel.hasPendingMessages()) {
                    final Message message = networkModel.popMessage();
                    sendMessage(message);
                }
                Thread.sleep(SLEEP_BETWEEN_EVENT_CHECKS);
            }
            handleDisconnect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.printf("ERROR while closing writer stream %s\n", e);
            }
        }
    }

    private void handleDisconnect() throws IOException {
        final UserDisconnectedMessage userDisconnectedMessage = new UserDisconnectedMessage();
        writer.writeObject(userDisconnectedMessage);
    }

    private void sendMessage(Message message) throws IOException {
        writer.writeObject(message);
    }
}

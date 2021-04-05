package org.chatting.client.model;

import org.chatting.common.message.Message;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NetworkModel {
    private final Queue<Message> pendingMessages = new ConcurrentLinkedQueue<>();
    private boolean shouldQuit;

    public boolean shouldQuit() {
        return shouldQuit;
    }

    public void setShouldQuit(boolean shouldQuit) {
        this.shouldQuit = shouldQuit;
    }

    public void sendMessage(Message message) {
        pendingMessages.add(message);
    }

    public boolean hasPendingMessages() {
        return pendingMessages.size() > 0;
    }

    public Message popMessage() {
        return pendingMessages.poll();
    }
}

package org.chatting.client.gui;

import org.chatting.client.gui.event.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EventQueue {

    private final Queue<Event> eventQueue = new ConcurrentLinkedQueue<>();

    public void pushEvent(Event event) {
        eventQueue.add(event);
    }

    public Event popEvent() {
        return eventQueue.poll();
    }

    public int size() {
        return eventQueue.size();
    }
}

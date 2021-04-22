package org.chatting.server.model;

import org.chatting.server.network.UserThread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ServerModel {

    private final Map<Long, UserThread> userThreads = new HashMap<>();

    public void removeUser(UserThread userThread) {
        userThreads.remove(userThread.getId());
    }

    public void addUser(UserThread userThread) {
        userThreads.put(userThread.getId(), userThread);
    }

    public Set<UserThread> getUserThreads() {
        return new HashSet<>(userThreads.values());
    }
}

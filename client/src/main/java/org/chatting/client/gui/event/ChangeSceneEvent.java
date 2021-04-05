package org.chatting.client.gui.event;

import org.chatting.client.gui.controller.SceneType;

public class ChangeSceneEvent implements Event {

    private final SceneType sceneType;

    public ChangeSceneEvent(SceneType sceneType) {
        this.sceneType = sceneType;
    }

    @Override
    public EventType getEventType() {
        return EventType.CHANGE_SCENE;
    }

    public SceneType getSceneType() {
        return sceneType;
    }
}

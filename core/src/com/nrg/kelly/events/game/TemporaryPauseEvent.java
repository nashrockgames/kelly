package com.nrg.kelly.events.game;

public class TemporaryPauseEvent {
    private final float pauseTime;

    public TemporaryPauseEvent(float pauseTime) {
        this.pauseTime = pauseTime;
    }

    public float getPauseTime() {
        return pauseTime;
    }
}

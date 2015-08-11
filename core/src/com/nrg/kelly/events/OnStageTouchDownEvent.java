package com.nrg.kelly.events;

/**
 * Created by Andrew on 24/06/2015.
 */
public class OnStageTouchDownEvent {

    private final float x;
    private final float y;

    public OnStageTouchDownEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

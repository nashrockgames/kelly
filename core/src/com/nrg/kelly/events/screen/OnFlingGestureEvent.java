package com.nrg.kelly.events.screen;

/**
 * Created by Andrew on 23/06/2015.
 */
public class OnFlingGestureEvent {

    private com.nrg.kelly.events.screen.FlingDirection flingDirection;

    public OnFlingGestureEvent(float velocityY) {
        if(Math.abs(velocityY) > 1) {
            this.flingDirection = velocityY > 0 ? com.nrg.kelly.events.screen.FlingDirection.DOWN : com.nrg.kelly.events.screen.FlingDirection.UP;
        } else {
            this.flingDirection = com.nrg.kelly.events.screen.FlingDirection.NONE;
        }
    }

    public com.nrg.kelly.events.screen.FlingDirection getFlingDirection() {
        return flingDirection;
    }
}

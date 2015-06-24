package com.nrg.kelly.events;

/**
 * Created by Andrew on 23/06/2015.
 */
public class OnFlingGestureEvent {

    private FlingDirection flingDirection;

    public OnFlingGestureEvent(float velocityY) {
        if(Math.abs(velocityY) > 1) {
            this.flingDirection = velocityY > 0 ? FlingDirection.UP : FlingDirection.DOWN;
        } else {
            this.flingDirection = FlingDirection.NONE;
        }
    }

    public FlingDirection getFlingDirection() {
        return flingDirection;
    }
}

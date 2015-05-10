package com.nrg.kelly.events;

/**
 * Created by Andrew on 10/05/2015.
 */
public class RightSideScreenTouchedEvent {

    private final int x;
    private final int y;
    private final int pointer;
    private final int button;

    public RightSideScreenTouchedEvent(int x, int y, int pointer, int button) {
        this.x = x;
        this.y = y;
        this.pointer = pointer;
        this.button = button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPointer() {
        return pointer;
    }

    public int getButton() {
        return button;
    }
}

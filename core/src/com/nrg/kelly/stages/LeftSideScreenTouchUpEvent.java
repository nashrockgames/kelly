package com.nrg.kelly.stages;

import com.nrg.kelly.events.screen.ScreenTouchedEvent;

/**
 * Created by Andrew on 25/05/2015.
 */
public class LeftSideScreenTouchUpEvent extends ScreenTouchedEvent {

    public LeftSideScreenTouchUpEvent(int x, int y, int pointer, int button) {
        super(x, y, pointer, button);
    }
}

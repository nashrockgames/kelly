package com.nrg.kelly.stages;

import com.badlogic.gdx.input.GestureDetector;
import com.nrg.kelly.screens.game.DirectionGestureListener;

import javax.inject.Inject;

/**
 * Created by Andrew on 23/06/2015.
 */
public class DirectionGestureAdapter extends GestureDetector {

    @Inject
    public DirectionGestureAdapter(DirectionGestureListener listener) {
        super(listener);
    }

}

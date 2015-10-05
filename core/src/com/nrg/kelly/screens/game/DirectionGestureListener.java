package com.nrg.kelly.screens.game;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.OnDoubleTapGestureEvent;
import com.nrg.kelly.events.screen.OnFlingGestureEvent;
import com.nrg.kelly.events.screen.OnTouchDownGestureEvent;

import javax.inject.Inject;

/**
 * Created by Andrew on 24/06/2015.
 */
public class DirectionGestureListener implements GestureDetector.GestureListener{

    @Inject
    public DirectionGestureListener(){

    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Events.get().post(new OnTouchDownGestureEvent(x, y));
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(count==2){
            Events.get().post(new OnDoubleTapGestureEvent(x, y));
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        Events.get().post(new OnFlingGestureEvent(velocityY));
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}

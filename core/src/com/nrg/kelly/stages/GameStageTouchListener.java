package com.nrg.kelly.stages;

import com.badlogic.gdx.math.Vector3;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.FlingDirection;
import com.nrg.kelly.events.screen.OnFlingGestureEvent;
import com.nrg.kelly.events.screen.OnStageTouchDownEvent;
import com.nrg.kelly.events.screen.OnTouchDownGestureEvent;
import com.nrg.kelly.events.game.JumpControlInvokedEvent;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;
import com.nrg.kelly.events.screen.OnStageTouchUpEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.stages.actors.PlayButtonActor;

import javax.inject.Inject;

/**
 * Created by Andrew on 11/08/2015.
 */
public class GameStageTouchListener {

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    GameStateManager gameStateManager;

    @Inject
    public GameStageTouchListener(){
        Events.get().register(this);
    }

    @Subscribe
    public void onStageTouchUp(OnStageTouchUpEvent onStageTouchUpEvent){

        final GameState gameState = gameStateManager.getGameState();
        if(gameState.equals(GameState.PLAYING)) {
            final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
            final Vector3 point = touchPoint.set(onStageTouchUpEvent.getX(),
                    onStageTouchUpEvent.getY(), 0);
            box2dGameStageView.translateScreenToWorldCoordinates(point);
            if (!box2dGameStageView.rightSideTouched(touchPoint)) {
                Events.get().post(new LeftSideScreenTouchUpEvent());
            }
        }

    }

    @Subscribe
    public void onStageTouchDown(OnStageTouchDownEvent onStageTouchDownEvent){
        final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
        final Vector3 point = touchPoint.set(onStageTouchDownEvent.getX(),
                onStageTouchDownEvent.getY(), 0);
        box2dGameStageView.translateScreenToWorldCoordinates(point);
        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PLAYING:
                if (box2dGameStageView.rightSideTouched(touchPoint)) {
                    Events.get().post(new JumpControlInvokedEvent());
                } else {
                    Events.get().post(new SlideControlInvokedEvent());
                }
                break;
            case PAUSED:
                if(box2dGameStageView.playButtonTouched(touchPoint)){
                    Events.get().post(new PlayButtonClickedEvent());
                }
        }
    }

    @Subscribe
    public void onTouchGesture(OnTouchDownGestureEvent onTouchDownGestureEvent){
        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PAUSED:
                if(this.box2dGameStageView.playButtonGestureTouched(onTouchDownGestureEvent.getX(),
                        onTouchDownGestureEvent.getY())){
                    Events.get().post(new PlayButtonClickedEvent());
                }
        }
    }

    @Subscribe
    public void onFlingGesture(OnFlingGestureEvent onFlingGestureEvent){
        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PLAYING:
                final FlingDirection flingDirection = onFlingGestureEvent.getFlingDirection();
                if(flingDirection.equals(FlingDirection.UP)){
                    Events.get().post(new JumpControlInvokedEvent());
                } else if(flingDirection.equals(FlingDirection.DOWN)){
                    Events.get().post(new SlideControlInvokedEvent());
                }
        }
    }


    public void init(PlayButtonActor playButtonActor) {
        this.box2dGameStageView.setupCamera();
        this.box2dGameStageView.setupTouchPoints(playButtonActor);
    }
}

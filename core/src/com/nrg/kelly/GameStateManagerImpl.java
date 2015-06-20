package com.nrg.kelly;

import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;

import javax.inject.Inject;

/**
 * Created by Andrew on 19/06/2015.
 */
public class GameStateManagerImpl implements GameStateManager{

    private GameState gameState;

    @Inject
    public GameStateManagerImpl(){
        Events.get().register(this);
    }

    @Subscribe
    public void onBuildGame(PostBuildGameModuleEvent postBuildGameModuleEvent){
        gameState = GameState.PAUSED;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }


}

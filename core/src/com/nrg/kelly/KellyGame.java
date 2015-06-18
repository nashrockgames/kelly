package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.screens.game.GameScreen;

import javax.inject.Inject;

/**
 * Created by Andrew on 29/04/2015.
 */
public class KellyGame extends Game {

    @Inject
    GameScreen gameScreen;

    @Inject
    public KellyGame(){
        Events.get().register(this);
    }

    @Override
    public void create() {
        this.setScreen(gameScreen);
    }


}

package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.nrg.kelly.screens.GameScreen;

import javax.inject.Inject;

/**
 * Created by Andrew on 29/04/2015.
 */
public class KellyGame extends Game {

    @Inject
    GameScreen screen;

    @Inject
    public KellyGame(){

    }

    @Override
    public void create() {
        this.setScreen(screen);
    }
}

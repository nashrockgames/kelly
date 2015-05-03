package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.screens.GameScreen;

import javax.inject.Inject;

/**
 * Created by Andrew on 29/04/2015.
 */
public class KellyGame extends Game {

    @Inject
    GameScreen screen;

    @Inject
    Config config;

    @Inject
    public KellyGame(){}

    @Override
    public void create() {
        if(config.getSettings().isDebug()){
            //create and add the debug text to the stage/ scren

        }
        this.setScreen(screen);
    }
}

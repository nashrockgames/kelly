package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.screens.GameScreen;
import com.nrg.kelly.screens.MainMenuScreen;

import javax.inject.Inject;

/**
 * Created by Andrew on 29/04/2015.
 */
public class KellyGame extends Game {

    @Inject
    MainMenuScreen screen;

    @Inject
    public KellyGame(){

    }

    @Override
    public void create() {
        this.setScreen(screen);
    }
}

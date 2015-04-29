package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.nrg.kelly.screens.GameScreen;

import javax.inject.Inject;


public class Main extends Game {

    @Inject GameScreen gameScreen;

    @Override
    public void create() {
        this.setScreen(gameScreen);
    }

}

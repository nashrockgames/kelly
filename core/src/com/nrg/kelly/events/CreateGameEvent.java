package com.nrg.kelly.events;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

/**
 * Created by Andrew on 3/05/2015.
 */
public class CreateGameEvent {

    private final ApplicationListener game;

    public CreateGameEvent(ApplicationListener game){
        this.game = game;
    }

}

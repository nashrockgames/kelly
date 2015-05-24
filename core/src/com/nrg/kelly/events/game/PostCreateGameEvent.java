package com.nrg.kelly.events.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

/**
 * Created by Andrew on 3/05/2015.
 */
public class PostCreateGameEvent {

    private final ApplicationListener game;

    public PostCreateGameEvent(ApplicationListener game){
        this.game = game;
    }

}

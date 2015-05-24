package com.nrg.kelly.events.game;

import com.badlogic.gdx.ApplicationListener;

/**
 * Created by Andrew on 3/05/2015.
 */
public class PostBuildGameModuleEvent {

    private final ApplicationListener game;

    public PostBuildGameModuleEvent(ApplicationListener game) {
        this.game = game;
    }

    public ApplicationListener getGame() {
        return game;
    }
}

package com.nrg.kelly.android;

import com.badlogic.gdx.ApplicationListener;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;

/**
 * Created by Andrew on 30/04/2015.
 */
public class AndroidDaggerAdapter implements ApplicationListener {

    private ApplicationListener game;

    @Override
    public void create() {

        final GameComponent gameComponent = DaggerGameComponent.builder().build();
        game = gameComponent.getKellyGame();
        game.create();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

}

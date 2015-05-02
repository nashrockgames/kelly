package com.nrg.kelly.desktop;

import com.badlogic.gdx.ApplicationListener;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;

/**
 * Created by Andrew on 30/04/2015.
 */
public class DesktopDaggerAdapter implements ApplicationListener{

    private ApplicationListener game;

    @Override
    public void create() {

        final GameComponent gameComponent = DaggerGameComponent.builder().build();
        game = gameComponent.getKellyGame();
        game.create();
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
    }

    @Override
    public void render() {
        game.render();
    }

    @Override
    public void pause() {
        game.pause();
    }

    @Override
    public void resume() {
        game.resume();
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}

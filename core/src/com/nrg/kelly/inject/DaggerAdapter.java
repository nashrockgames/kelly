package com.nrg.kelly.inject;

import com.badlogic.gdx.ApplicationListener;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;
import com.nrg.kelly.events.CreateGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Worlds;

/**
 * Created by Andrew on 2/05/2015.
 */
public class DaggerAdapter implements ApplicationListener {

    private ApplicationListener game;

    @Override
    public void create() {
        Worlds.init();
        final GameComponent gameComponent = DaggerGameComponent.builder().build();
        game = gameComponent.getKellyGame();
        game.create();
        Events.get().post(new CreateGameEvent(game));
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

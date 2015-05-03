package com.nrg.kelly.inject;

import com.badlogic.gdx.ApplicationListener;
import com.nrg.kelly.DaggerGameComponent;
import com.nrg.kelly.GameComponent;
import com.nrg.kelly.events.PostConstructGameEvent;
import com.nrg.kelly.events.DisposeGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.PreConstructGameEvent;
import com.nrg.kelly.physics.SceneFactory;

/**
 * Created by Andrew on 2/05/2015.
 */
public class DaggerAdapter implements ApplicationListener {

    private ApplicationListener game;

    @Override
    public void create() {
        initGlobalObjects();
        Events.get().post(new PreConstructGameEvent());
        constructObjects();
        Events.get().post(new PostConstructGameEvent(game));
    }

    private void constructObjects() {
        final GameComponent gameComponent = DaggerGameComponent.builder().build();
        game = gameComponent.getKellyGame();
        game.create();
    }

    private void initGlobalObjects() {

        SceneFactory.getInstance().init();

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
        Events.get().post(new DisposeGameEvent());
        game.dispose();
    }
}

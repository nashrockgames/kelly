package com.nrg.kelly.inject;

import com.badlogic.gdx.ApplicationListener;
import com.nrg.kelly.events.game.PreBuildGameModuleEvent;
import com.nrg.kelly.events.game.PostCreateGameEvent;
import com.nrg.kelly.events.game.DisposeGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.physics.Box2dFactory;

/**
 * Created by Andrew on 2/05/2015.
 */
public class DaggerAdapter implements ApplicationListener {

    private ApplicationListener game;

    @Override
    public void create() {
        Events.get().post(new PreBuildGameModuleEvent());
        game = Box2dFactory.getInstance().buildGame();
        Events.get().post(new PostBuildGameModuleEvent(game));
        game.create();
        Events.get().post(new PostCreateGameEvent(game));
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

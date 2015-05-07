package com.nrg.kelly.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.events.DrawGameStageEvent;
import com.nrg.kelly.events.game.PostConstructGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.SceneFactory;
import com.nrg.kelly.stages.text.DebugText;

import javax.inject.Inject;

/**
 * Created by Andrew on 26/04/2015.
 */
public class GameStage extends AbstractStage {

    private final static DrawGameStageEvent DRAW_GAME_STAGE_EVENT = new DrawGameStageEvent();
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    @Inject
    GameStageModel gameStageModel;

    @Inject
    Config gameConfig;

    @Inject
    GameStageView gameStageView;

    @Inject
    public GameStage() {
        Events.get().register(this);
    }

    @Subscribe
    public void setupActors(PostConstructGameEvent postConstructGameEvent){

        this.addActors(this.gameStageModel.getActors());
        if(gameConfig.getSettings().isDebug()) {
            this.addActor(new DebugText());
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            SceneFactory.getWorld().step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

    }

    @Override
    public void draw() {
        super.draw();
        Events.get().post(DRAW_GAME_STAGE_EVENT);
    }

    public void show() {
        this.gameStageView.setupCamera();
    }
}
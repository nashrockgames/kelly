package com.nrg.kelly.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.events.game.PostConstructGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;
import com.nrg.kelly.stages.text.DebugText;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Box2dGameStage extends AbstractStage {

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    @Inject
    Box2dGameModel box2dGameModel;

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    public Box2dGameStage() {
        Events.get().register(this);
    }

    @Subscribe
    public void setupActors(PostConstructGameEvent postConstructGameEvent){
        final List<Actor> actors = this.box2dGameModel.getActors();
        this.addActors(actors);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            Box2dFactory.getWorld().step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }

    }

    @Override
    public void draw() {
        super.draw();
        box2dGameStageView.renderGameStage();

    }

    public void show() {
        this.box2dGameStageView.setupCamera();
    }
}
package com.nrg.kelly.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.BeginContactEvent;
import com.nrg.kelly.events.RightSideScreenTouchedEvent;
import com.nrg.kelly.events.game.PostConstructGameEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;


import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Box2dGameStage extends AbstractStage implements ContactListener {

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    @Inject
    Box2dGameModel box2dGameModel;

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    public Box2dGameStage() {
        Events.get().register(this);
        Gdx.input.setInputProcessor(this);
        Box2dFactory.getWorld().setContactListener(this);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        box2dGameStageView.translateScreenToWorldCoordinates(x, y);

        if (box2dGameStageView.rightSideTouched()) {
            Events.get().post(new RightSideScreenTouchedEvent(x, y, pointer, button));
        }

        return super.touchDown(x, y, pointer, button);
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
        this.box2dGameStageView.setupTouchPoints();
    }

    @Override
    public void beginContact(Contact contact) {
        Events.get().post(new BeginContactEvent(contact));
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
package com.nrg.kelly.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.factories.ActorFactory;
import com.nrg.kelly.events.game.OnEnemyDestroyedEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.screen.*;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import javax.inject.Inject;

public class GameStageView extends AbstractStage implements ContactListener {

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private int level = 1;
    private int enemy = 0;

    @Inject
    Box2dGameModel box2dGameModel;

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    ActorFactory actorFactory;

    @Inject
    public GameStageView() {
        Events.get().register(this);
        Box2dFactory.getWorld().setContactListener(this);
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
        box2dGameStageView.translateScreenToWorldCoordinates(touchPoint.set(x, y, 0));
        if (box2dGameStageView.rightSideTouched(touchPoint)) {
            Events.get().post(new RightSideScreenTouchDownEvent(x, y, pointer, button));
        } else {
            Events.get().post(new LeftSideScreenTouchDownEvent(x, y, pointer, button));
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {

        final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
        box2dGameStageView.translateScreenToWorldCoordinates(touchPoint.set(x, y, 0));
        if(!box2dGameStageView.rightSideTouched(touchPoint)) {
            Events.get().post(new LeftSideScreenTouchUpEvent(x, y, pointer, button));
        }
        return super.touchUp(x, y, pointer, button);
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

    @Subscribe
    public void onEnemyDestroyed(OnEnemyDestroyedEvent onEnemyDestroyedEvent){
        enemy++;
        if(actorFactory.hasNextEnemy(level, enemy)) {
            this.addActor(actorFactory.createEnemy(level, enemy));
        }
    }

    public void show() {
        this.box2dGameStageView.setupCamera();
        this.box2dGameStageView.setupTouchPoints();
        this.addActor(actorFactory.createEnemy(level, enemy));
        this.addActor(actorFactory.createBackground(level));
        this.addActor(actorFactory.createGround(level));
        this.addActors(this.box2dGameModel.getActors());
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void beginContact(Contact contact) {
        Events.get().post(new BeginContactEvent(contact));
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}
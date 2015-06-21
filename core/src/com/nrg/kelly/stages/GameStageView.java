package com.nrg.kelly.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.events.GameOverEvent;
import com.nrg.kelly.inject.ActorFactory;
import com.nrg.kelly.events.game.OnEnemyDestroyedEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.screen.*;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.PlayButtonActor;

import javax.inject.Inject;
import javax.inject.Singleton;

public class GameStageView extends AbstractStage implements ContactListener {

    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private int level = 1;
    private int enemy = 0;

    @Inject
    GameStateManager gameStateManager;

    @Inject
    Box2dGameModel box2dGameModel;

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    ActorFactory actorFactory;

    @Inject
    PlayButtonActor playButtonActor;

    @Inject
    public GameStageView() {
        Events.get().register(this);
        Box2dFactory.getWorld().setContactListener(this);
    }
/*
    @Override
    public void draw(){
        super.draw();
        this.box2dGameStageView.renderGameStage();
    }
*/

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
        box2dGameStageView.translateScreenToWorldCoordinates(touchPoint.set(x, y, 0));
        final GameState gameState = gameStateManager.getGameState();

        switch(gameState){
            case PLAYING:
                if (box2dGameStageView.rightSideTouched(touchPoint)) {
                    Events.get().post(new RightSideScreenTouchDownEvent(x, y, pointer, button));
                } else {
                    Events.get().post(new LeftSideScreenTouchDownEvent(x, y, pointer, button));
                }
                break;
            case PAUSED:
                if(box2dGameStageView.playButtonTouched(touchPoint)){
                    Events.get().post(new PlayButtonClickedEvent());
                }
        }
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        final GameState gameState = gameStateManager.getGameState();
        if(gameState.equals(GameState.PLAYING)) {
            final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
            box2dGameStageView.translateScreenToWorldCoordinates(touchPoint.set(x, y, 0));
            if (!box2dGameStageView.rightSideTouched(touchPoint)) {
                Events.get().post(new LeftSideScreenTouchUpEvent(x, y, pointer, button));
            }
        }
        return super.touchUp(x, y, pointer, button);
    }

    @Override
    public void act(float delta) {
        final GameState gameState = this.gameStateManager.getGameState();
        if(gameState.equals(GameState.PLAYING)) {
            super.act(delta);
            // Fixed timestep
            accumulator += delta;
            while (accumulator >= delta) {
                Box2dFactory.getWorld().step(TIME_STEP,
                        VELOCITY_ITERATIONS, POSITION_ITERATIONS);
                accumulator -= TIME_STEP;
            }
        }
    }

    @Subscribe
    public void onEnemyDestroyed(OnEnemyDestroyedEvent onEnemyDestroyedEvent){
        final GameState gameState = this.gameStateManager.getGameState();
        if(gameState.equals(GameState.PLAYING)) {
            enemy++;
            spawnEnemy();
        }
    }

    private void spawnEnemy(){
        if(actorFactory.hasNextEnemy(level, enemy)) {
            this.addActor(actorFactory.createEnemy(level, enemy));
        }
    }

    @Subscribe
    public void onGameOver(GameOverEvent gameOverEvent){
        //remove any left over enemies
        gameOverEvent.getRunnerActor().remove();
        for(Actor actor : this.getActors()){
            if(actor instanceof EnemyActor){
                actor.remove();
            }
        }
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
    }

    @Subscribe
    public void addActors(PlayButtonClickedEvent playButtonClickedEvent){
        this.addActors(this.box2dGameModel.getActors());
        spawnEnemy();
    }

    public void show() {
        this.box2dGameStageView.setupCamera();
        this.box2dGameStageView.setupTouchPoints(this.playButtonActor);
        addBackgroundActors();

        this.addActor(playButtonActor);
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    private void addBackgroundActors() {
        this.addActor(actorFactory.createBackground(level));
        this.addActor(actorFactory.createGround(level));
    }
/*
    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK){
            Gdx.app.exit();
        }
        return false;
    }
    */

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
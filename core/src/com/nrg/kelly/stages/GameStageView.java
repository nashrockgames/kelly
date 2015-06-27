package com.nrg.kelly.stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.JointEdge;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.events.FlingDirection;
import com.nrg.kelly.events.OnFlingGestureEvent;
import com.nrg.kelly.events.GameOverEvent;
import com.nrg.kelly.events.OnTouchDownGestureEvent;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.events.game.TemporaryPauseEvent;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;
import com.nrg.kelly.events.screen.JumpControlInvokedEvent;
import com.nrg.kelly.inject.ActorFactory;
import com.nrg.kelly.events.game.OnEnemyDestroyedEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.PlayButtonActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import javax.inject.Inject;

public class GameStageView extends Stage implements ContactListener {

    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private int level = 1;
    private int enemyCount = 0;

    @Inject
    GameStateManager gameStateManager;

    @Inject
    Box2dGameStageView box2dGameStageView;

    @Inject
    DirectionGestureAdapter directionGestureAdapter;

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
        this.box2dGameStageView.debugGameStage();
    }
*/
    @Subscribe
    public void onTouchGesture(OnTouchDownGestureEvent onTouchDownGestureEvent){

        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PAUSED:
                if(this.box2dGameStageView.playButtonGestureTouched(onTouchDownGestureEvent.getX(),
                        onTouchDownGestureEvent.getY())){
                    Events.get().post(new PlayButtonClickedEvent());
                }
        }
    }

    @Subscribe
    public void onFlingGesture(OnFlingGestureEvent onFlingGestureEvent){

        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PLAYING:
                final FlingDirection flingDirection = onFlingGestureEvent.getFlingDirection();
                if(flingDirection.equals(FlingDirection.UP)){
                    Events.get().post(new JumpControlInvokedEvent());
                } else if(flingDirection.equals(FlingDirection.DOWN)){
                    Events.get().post(new SlideControlInvokedEvent());
                }
        }

    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {

        final Vector3 touchPoint = box2dGameStageView.getTouchPoint();
        box2dGameStageView.translateScreenToWorldCoordinates(touchPoint.set(x, y, 0));
        final GameState gameState = gameStateManager.getGameState();
        switch(gameState){
            case PLAYING:
                if (box2dGameStageView.rightSideTouched(touchPoint)) {
                    Events.get().post(new JumpControlInvokedEvent());
                } else {
                    Events.get().post(new SlideControlInvokedEvent());
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
                Events.get().post(new LeftSideScreenTouchUpEvent());
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
        if(enemyCount % 2 == 0){
            //TODO spawn boss first, then after a number of bullets, spawn armour, then spawn gun with 1 bullet, if you miss, then spawn after boss bullets again
            spawnArmour();
        } else {
            if(gameState.equals(GameState.PLAYING)) {
                spawnEnemy();
            }
        }
        enemyCount++;
    }

    @Subscribe
    public void onTemporaryPause(final TemporaryPauseEvent temporaryPauseEvent){

        gameStateManager.setGameState(GameState.TEMPORARILY_PAUSED);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                gameStateManager.setGameState(GameState.PLAYING);
            }
        }, temporaryPauseEvent.getPauseTime());

    }

    private void spawnArmour() {
        this.addActor(actorFactory.createArmour());
    }

    private void spawnEnemy(){
        if(actorFactory.hasNextEnemy(level, enemyCount)) {
            this.addActor(actorFactory.createEnemy(level, enemyCount));
        }
    }

    @Subscribe
    public void onGameOver(GameOverEvent gameOverEvent){
        //remove any left over enemies
        final RunnerActor runnerActor = gameOverEvent.getRunnerActor();
        final Body runnerBody = runnerActor.getBody();
        runnerBody.setUserData(null);
        final Array<JointEdge> jointList = runnerBody.getJointList();
        for(JointEdge jointEdge : jointList){
            Box2dFactory.getWorld().destroyJoint(jointEdge.joint);
        }
        Box2dFactory.destroyBody(runnerBody);
        runnerActor.remove();
        for(Actor actor : this.getActors()){
            if(actor instanceof EnemyActor){
                final Body body = ((EnemyActor) actor).getBody();
                body.setUserData(null);
                Box2dFactory.getWorld().destroyBody(body);
                actor.remove();
            }
        }
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
    }

    @Subscribe
    public void postBuildGame(PostBuildGameModuleEvent postBuildGameModuleEvent){
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
    }

    @Subscribe
    public void addActors(PlayButtonClickedEvent playButtonClickedEvent){
        final RunnerActor runner = actorFactory.createRunner();
        this.addActor(runner);
        spawnEnemy();
    }

    public void show() {
        this.box2dGameStageView.setupCamera();
        this.box2dGameStageView.setupTouchPoints(this.playButtonActor);
        addBackgroundActors();

        this.addActor(playButtonActor);
        final Application.ApplicationType type = Gdx.app.getType();
        if(type.equals(Application.ApplicationType.Desktop)) {
            Gdx.input.setInputProcessor(this);
        }else{
            Gdx.input.setInputProcessor(this.directionGestureAdapter);
        }
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
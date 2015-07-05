package com.nrg.kelly.stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.BossState;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.events.EnemySpawnTimeReducedEvent;
import com.nrg.kelly.events.FlingDirection;
import com.nrg.kelly.events.OnFlingGestureEvent;
import com.nrg.kelly.events.GameOverEvent;
import com.nrg.kelly.events.OnTouchDownGestureEvent;
import com.nrg.kelly.events.game.OnPlayTimeUpdatedEvent;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.events.game.HitArmourEvent;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;
import com.nrg.kelly.events.screen.JumpControlInvokedEvent;
import com.nrg.kelly.inject.ActorFactory;
import com.nrg.kelly.events.game.OnEnemySpawnedEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.ActorState;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.GameActor;
import com.nrg.kelly.stages.actors.PlayButtonActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class GameStageView extends Stage implements ContactListener {

    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 2;
    public static final float ENEMY_SPAWN_DELAY = 4.0f;
    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;
    private int level = 1;
    private Optional<Timer.Task> enemySchedule = Optional.absent();
    private Optional<RunnerActor> runner = Optional.absent();
    private float enemySpawnDelaySeconds = ENEMY_SPAWN_DELAY;
    private float enemyReduceSpawnDelaySeconds = 5.0f;
    private float enemyReduceSpawnDelayPercentage = 0.1f;
    private final float bossSpawnDelay = 10f;


    private Optional<Timer.Task> enemySpawnTimeReduceTask = Optional.absent();
    private float gameTime = 0f;

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

    private Optional<Timer.Task> gameTimeTask = Optional.absent();

    @Inject
    public GameStageView() {
        Events.get().register(this);
        Box2dFactory.getWorld().setContactListener(this);
    }

    @Subscribe
    public void onReduceEnemySpawnDelay(EnemySpawnTimeReducedEvent enemySpawnTimeReducedEvent){

        enemySpawnTimeReduceTask = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemySpawnDelaySeconds = enemySpawnDelaySeconds -
                        (enemySpawnDelaySeconds * enemyReduceSpawnDelayPercentage);
                Events.get().post(new EnemySpawnTimeReducedEvent());
            }
        }, enemyReduceSpawnDelaySeconds));

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
        if(!gameState.equals(GameState.PAUSED)) {
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

    private boolean canSpawnEnemy(RunnerActor runnerActor){

        final ActorState actorState = runnerActor.getActorState();
        final BossState bossState = gameStateManager.getBossState();
        return !bossState.equals(BossState.SPAWNING) &&
                !actorState.equals(ActorState.HIT) &&
                !actorState.equals(ActorState.FALLING);

    }

    @Subscribe
    public void onEnemySpawned(OnEnemySpawnedEvent onEnemySpawnedEvent){

        if(this.gameTime > 0f && this.gameTime % bossSpawnDelay == 0){
            /*TODO spawn boss first,
            // then after a number of bullets fired by boss,
            // spawn armour, then spawn gun with 1 bullet,
            // if you miss, then spawn gun after boss shoots a few bullets again
            */

            spawnBoss();
            /*
            if(runner.isPresent()) {
                if(!runner.get().getActorState().equals(ActorState.UPGRADING_ARMOUR)) {
                    if(!runner.get().getAnimationState().equals(AnimationState.ARMOUR_EQUIPPED)) {
                        this.spawnArmour();
                    }
                }
            }*/
        } else {
           this.enemySchedule = Optional.of(Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    if (gameStateManager.getGameState().equals(GameState.PLAYING)) {
                        for (final RunnerActor runnerActor : runner.asSet()) {
                            if (canSpawnEnemy(runnerActor)) {
                                spawnEnemy();
                            }
                        }
                    }
                }
            }, enemySpawnDelaySeconds));

        }

        /*
        final GameState gameState = this.gameStateManager.getGameState();
        if(enemyCount % 2 == 0){

            spawnArmour();
        } else {
            if(gameState.equals(GameState.PLAYING)) {
                spawnEnemy();
            }
        }
        */

    }

    @Subscribe
    public void onArmourHitEvent(final HitArmourEvent hitArmourEvent){

        gameStateManager.setGameState(GameState.HOLD_POSITION);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameStateManager.setGameState(GameState.PLAYING);
            }
        }, 0.25f);

    }

    private void spawnArmour() {
        this.addActor(actorFactory.createArmour());
    }

    private void spawnEnemy(){
            this.addActor(actorFactory.createEnemy(level));
            Events.get().post(new OnEnemySpawnedEvent());
    }

    private void spawnBoss(){
        this.addActor(actorFactory.createBoss(level));
        gameStateManager.setBossState(BossState.SPAWNING);
        //Events.get().post(new OnEnemySpawnedEvent());
    }

    @Subscribe
    public void onGameOver(GameOverEvent gameOverEvent){

        Gdx.app.log(this.getClass().getName(), "Total game time = " + this.gameTime + " seconds");

        if(this.enemySchedule.isPresent()){
            this.enemySchedule.get().cancel();
        }
        if(this.enemySpawnTimeReduceTask.isPresent()){
            enemySpawnTimeReduceTask.get().cancel();
        }
        if(gameTimeTask.isPresent()){
            gameTimeTask.get().cancel();
        }

        for(RunnerActor runnerActor : runner.asSet()) {
            destroyActor(runnerActor);
        }
        //remove any left over enemies by index
        final List<EnemyActor> enemyActorList = this.getEnemyActors();
        for(int index = 0; index < enemyActorList.size(); index++){
            final EnemyActor enemyActor = enemyActorList.get(index);
            destroyActor(enemyActor);
        }
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
        actorFactory.reset();
    }

    private List<EnemyActor> getEnemyActors(){
        final List<EnemyActor> enemyActors = new ArrayList<EnemyActor>();
        for(Actor actor : this.getActors()){
            if(actor instanceof EnemyActor){
                enemyActors.add((EnemyActor)actor);
            }
        }
        return enemyActors;
    }

    private void destroyActor(GameActor gameActor){

        final Body body = gameActor.getBody();
        body.setUserData(null);
        Box2dFactory.destroyBody(body);
        gameActor.remove();

    }

    @Subscribe
    public void postBuildGame(PostBuildGameModuleEvent postBuildGameModuleEvent) {
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
        this.gameStateManager.setBossState(BossState.NONE);
    }

    @Subscribe
    public void onPlayButtonClicked(PlayButtonClickedEvent playButtonClickedEvent){
        runner = Optional.of(actorFactory.createRunner());
        for(RunnerActor runnerActor : runner.asSet()) {
            this.addActor(runnerActor);
        }
        this.enemySpawnDelaySeconds = ENEMY_SPAWN_DELAY;
        this.gameTime = 0f;
        this.gameStateManager.setBossState(BossState.NONE);
        onPlayTimeUpdated(new OnPlayTimeUpdatedEvent());
        onReduceEnemySpawnDelay(new EnemySpawnTimeReducedEvent());
        spawnEnemy();
    }

    @Subscribe
    public void onPlayTimeUpdated(OnPlayTimeUpdatedEvent onPlayTimeUpdatedEvent) {

        gameTimeTask = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameTime += 1f;
                Events.get().post(new OnPlayTimeUpdatedEvent());
            }
        }, 1.0f));


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
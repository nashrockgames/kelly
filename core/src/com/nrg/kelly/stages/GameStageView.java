package com.nrg.kelly.stages;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.BossState;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.events.BombDroppedEvent;
import com.nrg.kelly.events.BulletFiredEvent;
import com.nrg.kelly.events.EnemySpawnTimeReducedEvent;
import com.nrg.kelly.events.FlingDirection;
import com.nrg.kelly.events.OnFlingGestureEvent;
import com.nrg.kelly.events.GameOverEvent;
import com.nrg.kelly.events.OnStageTouchDownEvent;
import com.nrg.kelly.events.OnTouchDownGestureEvent;
import com.nrg.kelly.events.SpawnEnemyEvent;
import com.nrg.kelly.events.SpawnGunEvent;
import com.nrg.kelly.events.game.CancelSchedulesEvent;
import com.nrg.kelly.events.game.OnPlayTimeUpdatedEvent;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.events.game.SpawnArmourEvent;
import com.nrg.kelly.events.game.SpawnBossBulletEvent;
import com.nrg.kelly.events.game.SpawnBossEvent;
import com.nrg.kelly.events.screen.OnStageTouchUpEvent;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;
import com.nrg.kelly.events.screen.JumpControlInvokedEvent;
import com.nrg.kelly.inject.ActorFactory;
import com.nrg.kelly.events.game.OnEnemySpawnedEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.ActorState;
import com.nrg.kelly.stages.actors.AnimationState;
import com.nrg.kelly.stages.actors.BossActor;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.EnemyBombActor;
import com.nrg.kelly.stages.actors.EnemyBulletActor;
import com.nrg.kelly.stages.actors.GameActor;
import com.nrg.kelly.stages.actors.PlayButtonActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class GameStageView extends Stage {

    private float accumulator = 0f;
    private int level = 1;
    private Optional<RunnerActor> runner = Optional.absent();
    private Optional<Timer.Task> gameTimeTask = Optional.absent();
    private Optional<Timer.Task> bossFireSchedule = Optional.absent();
    private float gameTime = 0f;

    @Inject
    GameConfig gameConfig;

    @Inject
    GameStateManager gameStateManager;

    @Inject
    DirectionGestureAdapter directionGestureAdapter;

    @Inject
    ActorFactory actorFactory;

    @Inject
    PlayButtonActor playButtonActor;

    @Inject
    GameStageContactListener gameStageContactListener;

    @Inject
    GameStageController gameStageController;

    @Inject
    GameStageTouchListener gameStageTouchListener;

    private float timeStep;

    @Inject
    public GameStageView() {
        Events.get().register(this);
    }

/*
    @Override
    public void draw(){
        super.draw();
        this.box2dGameStageView.debugGameStage();
    }
*/

    @Override
    public void act(float delta) {
        final GameState gameState = this.gameStateManager.getGameState();
        if(!gameState.equals(GameState.PAUSED)) {
            super.act(delta);
            // Fixed timestep
            accumulator += delta;
            while (accumulator >= delta) {
                Box2dFactory.getWorld().step(timeStep,
                        gameConfig.getWorldVelocityIterations(), gameConfig.getWorldPositionIterations());
                accumulator -= timeStep;
            }
        }
    }

    @Subscribe
    public void onBossDroppedBombEvent(BombDroppedEvent bombDroppedEvent) {
        if (gameStateManager.getGameState().equals(GameState.PLAYING)) {
            for(RunnerActor runnerActor : runner.asSet()) {
                this.spawnBossBomb(runnerActor);
            }
        }
    }

    @Subscribe
    public void spawnArmour(SpawnArmourEvent spawnArmourEvent) {
        this.addActor(actorFactory.createArmour());
    }

    @Subscribe
    public void spawnGun(SpawnGunEvent spawnGunEvent) {
        this.addActor(actorFactory.createGun());
    }

    @Subscribe
    public void spawnBossBullet(SpawnBossBulletEvent spawnBossBulletEvent) {
        final EnemyBulletActor bossBullet = this.actorFactory.createBossBullet(this.level);
        bossFireSchedule = spawnBossBulletEvent.getBossActor().getFireBulletSchedule();
        this.gameStateManager.setBossState(BossState.FIRING);
        this.addActor(bossBullet);
    }

    private EnemyBombActor spawnBossBomb(RunnerActor runnerActor) {
        final EnemyBombActor enemyBombActor = this.actorFactory.createBossBomb(this.level, runnerActor);
        this.gameStateManager.setBossState(BossState.DROPPING_BOMB);
        this.addActor(enemyBombActor);
        return enemyBombActor;
     }


    @Subscribe
    public void spawnEnemy(SpawnEnemyEvent spawnEnemyEvent){
        this.addActor(actorFactory.createEnemy(level));
        Events.get().post(new OnEnemySpawnedEvent(spawnEnemyEvent.getRunner()));
    }
    @Subscribe
    public void spawnBoss(SpawnBossEvent spawnBossEvent){
        this.addActor(actorFactory.createBoss(runner, level));
    }

    @Subscribe
    public void onGameOver(GameOverEvent gameOverEvent){

        Gdx.app.log(this.getClass().getName(), "Total game time = " + this.gameTime + " seconds");

        Events.get().post(new CancelSchedulesEvent());


        for(RunnerActor runnerActor : runner.asSet()) {
            destroyActor(runnerActor);
        }
        //remove any left over enemies by index
        final List<EnemyActor> enemyActorList = this.getEnemyActors();
        for(int index = 0; index < enemyActorList.size(); index++){
            final EnemyActor enemyActor = enemyActorList.get(index);
            destroyActor(enemyActor);
        }
        this.gameStageController.setEnemiesSpawned(0);
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
        actorFactory.reset();
    }

    @Subscribe
    public void cancelSchedules(CancelSchedulesEvent cancelSchedulesEvent) {

        if(gameTimeTask.isPresent()){
            gameTimeTask.get().cancel();
        }
        if(bossFireSchedule.isPresent()){
            bossFireSchedule.get().cancel();
        }
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
        this.timeStep = 1 / gameConfig.getWorldTimeStepDenominator();
        this.gameStageController.setEnemySpawnDelaySeconds(gameConfig.getEnemySpawnDelaySeconds());
        this.gameStageController.setReduceEnemySpawnIntervalSeconds(gameConfig.getReduceEnemySpawnIntervalSeconds());
        this.gameStageController.setReduceEnemySpawnDelayPercentage(gameConfig.getReduceEnemySpawnDelayPercentage());
        this.gameStageController.setSpawnBossOnEnemyCount(gameConfig.getSpawnBossOnEnemyCount());
        this.addActor(playButtonActor);
        this.gameStateManager.setGameState(GameState.PAUSED);
        this.gameStateManager.setBossState(BossState.NONE);
        Box2dFactory.getWorld().setContactListener(this.gameStageContactListener);

    }


    @Subscribe
    public void onPlayButtonClicked(PlayButtonClickedEvent playButtonClickedEvent){
        runner = Optional.of(actorFactory.createRunner());
        for(RunnerActor runnerActor : runner.asSet()) {
            this.addActor(runnerActor);
        }
        this.gameStageController.setEnemySpawnDelaySeconds(this.gameConfig.getEnemySpawnDelaySeconds());
        this.gameTime = 0f;
        this.gameStateManager.setBossState(BossState.NONE);
        onPlayTimeUpdated(new OnPlayTimeUpdatedEvent());
        spawnEnemy(new SpawnEnemyEvent(runner));
        Events.get().post(new EnemySpawnTimeReducedEvent());
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

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        Events.get().post(new OnStageTouchDownEvent(x, y));
        return super.touchDown(x, y, pointer, button);
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        Events.get().post(new OnStageTouchUpEvent(x, y));
        return super.touchUp(x, y, pointer, button);
    }

    public void show() {
        this.gameStageTouchListener.init(this.playButtonActor);
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

}
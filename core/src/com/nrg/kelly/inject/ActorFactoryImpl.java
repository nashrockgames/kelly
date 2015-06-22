package com.nrg.kelly.inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.config.factories.EnemyIndexConsumer;
import com.nrg.kelly.config.actors.Enemy;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.config.levels.LevelsConfig;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.ActorState;
import com.nrg.kelly.stages.actors.BackgroundActor;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActorFactoryImpl implements ActorFactory{


    final static Map<Integer,List<Integer>> linkedListMap =
            Collections.synchronizedMap(new HashMap<Integer, List<Integer>>());


    private LevelsConfig config;

    private CameraConfig cameraConfig;

    @Inject
    public ActorFactoryImpl(LevelsConfig levelsConfig, CameraConfig cameraConfig){

        this.cameraConfig = cameraConfig;
        config = levelsConfig;
        this.createEnemyIndices();

    }

    private void createEnemyIndices() {
        final LinkedList<LevelConfig> levels = config.getLevels();
        final EnemyIndexConsumer levelConfigEnemyIndexConsumer =
                new EnemyIndexConsumer(linkedListMap);
        for(LevelConfig levelConfig : levels) {
            levelConfigEnemyIndexConsumer.accept(levelConfig);
        }
    }

    public Actor createEnemy(int level, int enemyCount) {

        final int enemyIndex =  linkedListMap.get(level).get(enemyCount);
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        final List<Enemy> enemies = levelConfig.getEnemies();
        final Enemy enemy = enemies.get(enemyIndex);
        final EnemyActor enemyActor = new EnemyActor(enemy, cameraConfig);
        final List<AtlasConfig> animations = enemy.getAnimations();
        if(animations != null) {
            final AtlasConfig defaultAtlasConfig = enemyActor
                    .getAtlasConfigByName(animations, "default");
            enemyActor.setDefaultAtlasConfig(defaultAtlasConfig);
            final TextureAtlas defaultAtlas =
                    new TextureAtlas(Gdx.files.internal(defaultAtlasConfig.getAtlas()));
            enemyActor.setDefaultAnimation(new Animation(enemy.getFrameRate(),
                    defaultAtlas.getRegions()));
        }
        enemyActor.setLinearVelocity(new Vector2(enemy.getVelocityX(), 0f));
        return enemyActor;
    }

    public boolean hasNextEnemy(int level, int enemy) {
        return linkedListMap.keySet().contains(level) &&
                linkedListMap.get(level).size() > enemy;
    }

    public Actor createBackground(int level){
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        return new BackgroundActor(levelConfig.getBackground(), cameraConfig);
    }

    public Actor createGround(int level){
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        final String ground = levelConfig.getGround();
        return new GroundActor(gameConfig.getActors().getGround(), ground, cameraConfig);
    }

    public RunnerActor createRunner(){
        final GameConfig gameConfig = ConfigFactory.getGameConfig();
        final Runner runner = gameConfig.getActors().getRunner();
        final Body body = Box2dFactory.getInstance().createRunner();
        RunnerActor runnerActor = new RunnerActor(runner, cameraConfig);
        body.setUserData(runnerActor);
        runnerActor.setBody(body);
        runnerActor.setActorState(ActorState.RUNNING);
        runnerActor.resetPosition();
        return runnerActor;
    }

}

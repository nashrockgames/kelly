package com.nrg.kelly.config.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.config.levels.Enemy;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.config.levels.LevelsConfig;
import com.nrg.kelly.stages.actors.BackgroundActor;
import com.nrg.kelly.stages.actors.EnemyActor;
import com.nrg.kelly.stages.actors.GroundActor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class ActorFactory {


    final static Map<Integer,List<Integer>> linkedListMap =
            Collections.synchronizedMap(new HashMap<Integer, List<Integer>>());

    private static ActorFactory instance;
    private static LevelsConfig config;

    public static ActorFactory getInstance(LevelsConfig levelsConfig){
        if(instance==null){
            config = levelsConfig;
            instance = new ActorFactory();
            instance.createEnemyIndices();
        }
        return instance;
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
        final EnemyActor enemyActor = new EnemyActor(enemy);
        enemyActor.setLinearVelocity(new Vector2(enemy.getVelocityX(), 0f));
        return enemyActor;

    }

    public boolean hasNextEnemy(int level, int enemy) {
        return linkedListMap.keySet().contains(level) &&
                linkedListMap.get(level).size() > enemy;
    }

    public Actor createBackground(int level){
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        return new BackgroundActor(levelConfig.getBackground());
    }

    public Actor createGround(int level){
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        final String ground = levelConfig.getGround();
        return new GroundActor(ground);
    }


}

package com.nrg.kelly.config.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.config.levels.Enemy;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.config.levels.LevelsConfig;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.stages.actors.EnemyActor;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class EnemyFactory {


    final static Map<Integer,List<Integer>> linkedListMap =
            Collections.synchronizedMap(new HashMap<Integer, List<Integer>>());

    private static EnemyFactory instance;
    private static LevelsConfig config;

    public static EnemyFactory getInstance(LevelsConfig levelsConfig){
        if(instance==null){
            config = levelsConfig;
            instance = new EnemyFactory();
            instance.createEnemyIndices();
        }
        return instance;
    }

    private void createEnemyIndices() {
        final LinkedList<LevelConfig> levels = config.getLevels();
        levels.forEach(new Consumer<LevelConfig>() {
            @Override
            public void accept(LevelConfig levelConfig) {
                final List<Integer> enemyIndices = new LinkedList<Integer>();
                final List<Enemy> enemies = levelConfig.getEnemies();
                final int enemyCount = enemies.size();
                final Random random = new Random(levelConfig.getEnemySeed());
                for(int i = 0 ; i < levelConfig.getEnemyCount() ; i++){
                    enemyIndices.add(random.nextInt(enemyCount));
                }
                linkedListMap.put(linkedListMap.entrySet().size() + 1, enemyIndices);
            }
        });
    }

    public Actor createEnemy(int level, int enemyCount) {

        final int enemyIndex =  linkedListMap.get(level).get(enemyCount);
        final LevelConfig levelConfig = config.getLevels().get(level - 1);
        final List<Enemy> enemies = levelConfig.getEnemies();
        final Enemy enemy = enemies.get(enemyIndex);
        final EnemyActor enemyActor = new EnemyActor(Box2dFactory.getInstance().createEnemy(enemy));
        enemyActor.setLinearVelocity(new Vector2(enemy.getVelocityX(), 0f));
        return enemyActor;

    }
}

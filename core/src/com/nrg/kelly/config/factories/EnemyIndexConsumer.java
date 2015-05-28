package com.nrg.kelly.config.factories;

import com.nrg.kelly.config.levels.Enemy;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.util.Consumer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EnemyIndexConsumer implements Consumer<LevelConfig>{

    private final Map<Integer, List<Integer>> integerListMap;

    public EnemyIndexConsumer(Map<Integer,List<Integer>> integerListMap){
        this.integerListMap = integerListMap;
    }

    @Override
    public void accept(LevelConfig levelConfig) {
        final List<Integer> enemyIndices = new LinkedList<Integer>();
        final List<Enemy> enemies = levelConfig.getEnemies();
        final int enemyCount = enemies.size();
        final Random random = new Random(levelConfig.getEnemySeed());
        for(int i = 0 ; i < levelConfig.getEnemyCount() ; i++){
            enemyIndices.add(random.nextInt(enemyCount));
        }
        integerListMap.put(integerListMap.entrySet().size() + 1, enemyIndices);
        }
    }

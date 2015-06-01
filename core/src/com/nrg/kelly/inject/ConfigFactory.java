package com.nrg.kelly.inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.levels.LevelConfig;
import com.nrg.kelly.config.levels.LevelsConfig;

import java.util.LinkedList;

/**
 * Created by Andrew on 3/05/2015.
 */
public class ConfigFactory {

    private static GameConfig gameConfig;
    private static LevelsConfig levelsConfig;
    private static Json json  = new Json();

    public static GameConfig getGameConfig(){
        if(gameConfig == null) {
            gameConfig = loadGameConfig();
        }
        return gameConfig;
    }

    public static LevelsConfig getLevelsConfig(){
        if(levelsConfig == null) {
            levelsConfig = new LevelsConfig();
            levelsConfig.setLevels(loadLevelConfig(gameConfig));
        }
        return levelsConfig;
    }

    private static LinkedList<LevelConfig> loadLevelConfig(GameConfig gameConfig) {

        final int levelCount = gameConfig.getLevelCount();
        final LinkedList<LevelConfig> linkedList = new LinkedList<LevelConfig>();
        for(int i = 0; i<levelCount; i++){
            linkedList.add(json.fromJson(LevelConfig.class,
                    Gdx.files.internal("level_" + (i + 1) + ".json")));
        }
        return linkedList;

    }

    private static GameConfig loadGameConfig(){
        final FileHandle file = Gdx.files.internal("kelly.json");
        return json.fromJson(GameConfig.class, file);
    }

}




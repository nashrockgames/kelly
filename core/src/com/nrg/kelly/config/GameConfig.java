package com.nrg.kelly.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Andrew on 3/05/2015.
 */
public class GameConfig {

    private static Config config;

    public static Config getConfig(){
        if(config == null)
            config = loadConfig();
        return config;
    }

    private static Config loadConfig(){

        final Json json  = new Json();
        final FileHandle file = Gdx.files.internal("kelly.json");
        return json.fromJson(Config.class, file);
    }

}




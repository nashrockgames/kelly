package com.nrg.kelly.inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.physics.Worlds;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {
    @Provides
    World provideWorld(){
        return Worlds.getWorld();
    }

    @Provides
    Config provideConfig(){

        return GameConfig.getConfig();

    }

}

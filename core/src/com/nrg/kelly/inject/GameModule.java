package com.nrg.kelly.inject;

import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.physics.SceneFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {
    @Provides
    World provideWorld(){
        return SceneFactory.getWorld();
    }
    @Provides
    Config provideConfig(){
        return ConfigFactory.getConfig();
    }

}

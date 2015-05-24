package com.nrg.kelly.inject;

import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.physics.Box2dFactory;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                ModelModule.class,
                ViewModule.class
        }
)
public class GameModule {

    // This will be our viewport measurements while working with the debug renderer

    @Provides
    World provideWorld(){
        return Box2dFactory.getWorld();
    }
    @Provides
    Config provideConfig(){
        return ConfigFactory.getConfig();
    }

}

package com.nrg.kelly.inject;

import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.config.factories.ActorFactory;
import com.nrg.kelly.config.levels.LevelsConfig;
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

    @Provides
    World provideWorld(){
        return Box2dFactory.getWorld();
    }
    @Provides
    GameConfig provideGameConfig(){
        return ConfigFactory.getGameConfig();
    }
    @Provides
    LevelsConfig provideLevelConfig(){
        return ConfigFactory.getLevelsConfig();
    }
    @Provides
    ActorFactory provideEnemyFactory(LevelsConfig config){
        return ActorFactory.getInstance(config);
    }

}

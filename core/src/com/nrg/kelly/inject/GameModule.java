package com.nrg.kelly.inject;

import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.GameStateManagerImpl;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.config.buttons.PlayButtonConfig;
import com.nrg.kelly.config.levels.LevelsConfig;
import com.nrg.kelly.physics.Box2dFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
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
    @Singleton
    GameStateManager provideGameStateManager(){
        return new GameStateManagerImpl();
    }

    @Provides
    @Singleton
    ActorFactory provideEnemyFactory(LevelsConfig levelsConfig, CameraConfig cameraConfig){
        return new ActorFactoryImpl(levelsConfig, cameraConfig);
    }
    @Provides
    Runner provideRunnerConfig(GameConfig gameConfig){
        return gameConfig.getActors().getRunner();
    }

    @Provides
    PlayButtonConfig providePlayButtonConfig(GameConfig gameConfig){
        return gameConfig.getActors().getPlayButton();
    }

    @Provides
    Box2dFactory provideBox2dFactory(){
        return Box2dFactory.getInstance();
    }

}

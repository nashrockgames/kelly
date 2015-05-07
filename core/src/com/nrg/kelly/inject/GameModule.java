package com.nrg.kelly.inject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.ConfigFactory;
import com.nrg.kelly.physics.SceneFactory;
import com.nrg.kelly.stages.GameStageView;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    @Provides
    World provideWorld(){
        return SceneFactory.getWorld();
    }
    @Provides
    Config provideConfig(){
        return ConfigFactory.getConfig();
    }
    @Provides
    Camera provideCamera(){
        return new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    @Provides
    GameStageView provideGameStageView(Camera camera){
        GameStageView gameStageView = new GameStageView();
        gameStageView.setCamera(camera);
        gameStageView.setDebugRenderer(new Box2DDebugRenderer());
        return gameStageView;
    }


}

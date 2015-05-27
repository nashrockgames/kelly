package com.nrg.kelly.inject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nrg.kelly.stages.Box2dGameStageView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 10/05/2015.
 */
@Module
public class ViewModule {

    private static final int VIEWPORT_WIDTH = 20;
    private static final int VIEWPORT_HEIGHT = 13;

    @Provides
    Camera provideCamera(){
        return new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
    }

    @Provides
    Box2dGameStageView provideGameStageView(Camera camera){
        Box2dGameStageView box2dGameStageView = new Box2dGameStageView();
        box2dGameStageView.setCamera(camera);
        box2dGameStageView.setDebugRenderer(new Box2DDebugRenderer());
        return box2dGameStageView;
    }

}

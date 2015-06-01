package com.nrg.kelly.inject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.stages.Box2dGameStageView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 10/05/2015.
 */
@Module
public class ViewModule {

    @Provides
    Camera provideCamera(GameConfig config){

        final float viewPortWidth = config.getViewPortWidth();
        final float viewPortHeight = config.getViewPortHeight();
        final OrthographicCamera orthographicCamera =
                new OrthographicCamera(viewPortWidth, viewPortHeight);
        final FillViewport viewport =
                new FillViewport(viewPortWidth, viewPortHeight, orthographicCamera);
        viewport.apply();
        return orthographicCamera;
    }

    @Provides
    Box2dGameStageView provideGameStageView(Camera camera){
        Box2dGameStageView box2dGameStageView = new Box2dGameStageView();
        box2dGameStageView.setCamera(camera);
        box2dGameStageView.setDebugRenderer(new Box2DDebugRenderer());
        return box2dGameStageView;
    }

}

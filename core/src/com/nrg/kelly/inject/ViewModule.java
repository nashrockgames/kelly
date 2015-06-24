package com.nrg.kelly.inject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.stages.Box2dGameStageView;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModule {

    @Provides
    Camera provideCamera(GameConfig config){

        final float viewPortWidth = config.getViewPortWidth();
        final float viewPortHeight = config.getViewPortHeight();
        final OrthographicCamera camera = new OrthographicCamera(viewPortWidth, viewPortHeight);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();

        return camera;
    }

    @Provides
    Box2dGameStageView provideGameStageView(Camera camera, CameraConfig cameraConfig){
        Box2dGameStageView box2dGameStageView = new Box2dGameStageView();
        box2dGameStageView.setCamera(camera);
        box2dGameStageView.setDebugRenderer(new Box2DDebugRenderer());
        box2dGameStageView.setCameraConfig(cameraConfig);
        return box2dGameStageView;
    }

    @Provides
    CameraConfig provideCameraConfig(){
        final CameraConfig cameraConfig = new CameraConfig();
        final int screenHeight = Gdx.graphics.getHeight();
        final int scale = ((screenHeight / 6) / 64) * 64;
        if(scale < 64){
            cameraConfig.setWorldToScreenScale(64);
        } else {
            cameraConfig.setWorldToScreenScale(scale);
        }
        return cameraConfig;
    }

}


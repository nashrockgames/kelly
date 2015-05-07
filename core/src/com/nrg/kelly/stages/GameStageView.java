package com.nrg.kelly.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.DrawGameStageEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.SceneFactory;

import javax.inject.Inject;

/**
 * Created by Andrew on 7/05/2015.
 */
public class GameStageView {

    private Camera camera;
    private Box2DDebugRenderer box2DDebugRenderer;

    @Inject
    public GameStageView(){
        Events.get().register(this);
    }
    public void setDebugRenderer(Box2DDebugRenderer box2DDebugRenderer) {
        this.box2DDebugRenderer = box2DDebugRenderer;
    }

    public void setupCamera(){
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    @Subscribe
    public void drawGameStage(final DrawGameStageEvent drawGameStageEvent){
        this.box2DDebugRenderer.render(SceneFactory.getWorld(), camera.combined);

    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}

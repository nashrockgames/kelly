package com.nrg.kelly.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.stages.actors.PlayButtonActor;

import javax.inject.Inject;

/**
 * Created by Andrew on 7/05/2015.
 */
public class Box2dGameStageView {

    private Camera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Vector3 touchPoint;
    private Rectangle screenRightSide;
    private Rectangle playButtonRectangle;

    @Inject
    public Box2dGameStageView(){
        Events.get().register(this);
    }

    public void setDebugRenderer(Box2DDebugRenderer box2DDebugRenderer) {
        this.box2DDebugRenderer = box2DDebugRenderer;
    }

    public void setupCamera(){
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    public void setupTouchPoints(PlayButtonActor playButtonActor){

        touchPoint = new Vector3();
        screenRightSide = new Rectangle(camera.viewportWidth / 2, 0, camera.viewportWidth / 2,
        camera.viewportHeight);
        setupPlayButtonTouchPoint(playButtonActor);

    }

    private void setupPlayButtonTouchPoint(PlayButtonActor playButtonActor) {
        final ActorConfig actorConfig = playButtonActor.getConfig().get();
        final float playButtonX = actorConfig.getPosition().getX();
        final float playButtonY = actorConfig.getPosition().getY();
        final float width = actorConfig.getWidth();
        final float height = actorConfig.getHeight();
        playButtonRectangle = new Rectangle(playButtonX - width / 2f, playButtonY - height / 2f,
                width, height);
    }

    /*
        public void renderGameStage(){
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            this.box2DDebugRenderer.render(Box2dFactory.getWorld(), camera.combined);
        }
    */
    public Vector3 translateScreenToWorldCoordinates(Vector3 touchPoint) {
        return camera.unproject(touchPoint);
    }

    public boolean rightSideTouched(Vector3 touchPoint) {
        return screenRightSide.contains(touchPoint.x, touchPoint.y);
    }

    public boolean playButtonTouched(Vector3 touchPoint){
        return playButtonRectangle.contains(touchPoint.x, touchPoint.y);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vector3 getTouchPoint() {
        return touchPoint;
    }

}

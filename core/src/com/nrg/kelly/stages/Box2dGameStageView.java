package com.nrg.kelly.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;

import javax.inject.Inject;

/**
 * Created by Andrew on 7/05/2015.
 */
public class Box2dGameStageView {

    private Camera camera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Vector3 touchPoint;
    private Rectangle screenRightSide;

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

    public void setupTouchPoints(){

        touchPoint = new Vector3();
        screenRightSide = new Rectangle(camera.viewportWidth / 2, 0, camera.viewportWidth / 2,
        camera.viewportHeight);

    }

    public void renderGameStage(){
        this.box2DDebugRenderer.render(Box2dFactory.getWorld(), camera.combined);
    }

    /**
     * Helper function to get the actual coordinates in my world
     * @param x
     * @param y
     */
    public void translateScreenToWorldCoordinates(int x, int y) {
        camera.unproject(touchPoint.set(x, y, 0));
    }

    public boolean rightSideTouched() {
        return screenRightSide.contains(touchPoint.x, touchPoint.y);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
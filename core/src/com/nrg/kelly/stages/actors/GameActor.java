package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.Constants;

public abstract class GameActor extends Actor {

    private Body body;

    protected Rectangle screenRectangle = new Rectangle();

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        final Object userData = body.getUserData();
        if (userData != null) {
            updateRectangle((GameActor)userData);
        }

    }

    private void updateRectangle(GameActor userData) {
        screenRectangle.x = transformToScreen(body.getPosition().x - userData.getWidth() / 2);
        screenRectangle.y = transformToScreen(body.getPosition().y - userData.getHeight() / 2);
        screenRectangle.width = transformToScreen(userData.getWidth());
        screenRectangle.height = transformToScreen(userData.getHeight());
    }

    protected float transformToScreen(float n) {
        return Constants.WORLD_TO_SCREEN * n;
    }
}

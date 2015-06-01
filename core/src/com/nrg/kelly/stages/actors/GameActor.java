package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.Constants;

public abstract class GameActor extends Actor {

    private Body body;

    protected Rectangle textureBounds
            = new Rectangle();

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final Body body = this.getBody();
        if(body!=null) {
            final Object userData = body.getUserData();
            if (userData != null) {
                updateRectangle(this);
            }
        }
    }

    protected void updateRectangle(GameActor gameActor) {

        //get the screen width

        textureBounds.x = transformToScreen(body.getPosition().x - gameActor.getWidth() / 2);
        textureBounds.y = transformToScreen(body.getPosition().y - gameActor.getHeight() / 2);
        textureBounds.width = transformToScreen(gameActor.getWidth());
        textureBounds.height = transformToScreen(gameActor.getHeight());
    }

    protected float transformToScreen(float n) {
        //TODO: multiply by height and width aspect ratios
        //image texture width and height with screen vs actor width and height with stage
        return Constants.WORLD_TO_SCREEN * n;
    }
}

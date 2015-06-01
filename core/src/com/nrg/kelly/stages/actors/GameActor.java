package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.Constants;

public abstract class GameActor extends Actor {

    private Body body;

    protected Rectangle textureBounds
            = new Rectangle();
    private int textureWidth;

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
                updateTextureBounds(this);
            }
        }
    }

    protected void updateTextureBounds(GameActor gameActor) {

        //get the screen width

        textureBounds.x = transformToScreenX(body.getPosition().x - gameActor.getWidth() / 2);
        textureBounds.y = transformToScreenY(body.getPosition().y - gameActor.getHeight() / 2);
        textureBounds.width = transformToScreenX(gameActor.getWidth());
        textureBounds.height = transformToScreenY(gameActor.getHeight());
    }

    protected float transformToScreenX(float n) {
        //TODO: multiply by height and width aspect ratios
        float textureWidthRatio = this.getTextureWidth() / Constants.APP_WIDTH;
        float stageWidthRatio;
        //image texture width and height with screen vs actor width and height with stage
        return Constants.WORLD_TO_SCREEN * n;
    }
    protected float transformToScreenY(float n) {
        //TODO: multiply by height and width aspect ratios
        float widthRatio = this.getTextureHeight() / Constants.APP_WIDTH;

        //TODO: multiply by height and width aspect ratios
        //image texture width and height with screen vs actor width and height with stage
        return Constants.WORLD_TO_SCREEN * n;
    }

    public abstract float getTextureWidth();

    public abstract float getTextureHeight();
}

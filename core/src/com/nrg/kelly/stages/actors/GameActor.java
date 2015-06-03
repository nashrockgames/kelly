package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.base.Optional;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.inject.ConfigFactory;

public abstract class GameActor extends Actor {

    protected Optional<ActorConfig> config;
    private Body body;

    private Rectangle textureBounds
            = new Rectangle();

    public GameActor(ActorConfig config) {
            this.config = Optional.fromNullable(config);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        maybeUpdateTextureBounds();
    }

    protected void maybeUpdateTextureBounds(){
        if(this.getConfig().isPresent()) {
            final Body body = this.getBody();
            if (body != null) {
                final Object userData = body.getUserData();
                if (userData instanceof GameActor) {
                    updateTextureBounds(this.getConfig().get());
                }
            }
        }
    }

    protected void updateTextureBounds(ActorConfig config) {
        //get the screen width
        final Rectangle textureBounds = this.getTextureBounds();
        final float bodyWidth = getWidth();
        final float bodyHeight = getHeight();
        textureBounds.x = transformToScreenX(getBody().getPosition().x - bodyWidth / 2);
        textureBounds.y = transformToScreenY(getBody().getPosition().y - bodyHeight / 2);
        textureBounds.width = transformToScreenX(bodyWidth);
        textureBounds.height = transformToScreenY(bodyHeight);
    }

    protected float transformToScreenX(float x) {
        return x * 64;
    }
    protected float transformToScreenY(float y) {
        return y * 64;

    }
    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getTextureBounds() {
        return textureBounds;
    }

    public void setTextureBounds(Rectangle textureBounds) {
        this.textureBounds = textureBounds;
    }

    public Optional<ActorConfig> getConfig() {
        return config;
    }
}

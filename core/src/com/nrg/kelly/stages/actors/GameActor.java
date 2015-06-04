package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.base.Optional;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.ImageOffset;

import java.util.List;

public abstract class GameActor extends Actor {

    public static final int WORLD_TO_SCREEN = 64;
    protected Optional<ActorConfig> config;
    private Body body;

    private Rectangle textureBounds
            = new Rectangle();

    public GameActor(ActorConfig config) {
            this.config = Optional.fromNullable(config);
    }

    protected AtlasConfig getAtlasConfigByName(List<AtlasConfig> atlasConfigList, String name){
        for(AtlasConfig a: atlasConfigList){
            if(a.getName().equals(name)){
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown atlas name " + name);
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

    protected void drawAnimation(Batch batch, TextureRegion textureRegion, Optional<ImageOffset> offsetOptional){

        float x = textureBounds.x;
        float y = textureBounds.y;
        if(offsetOptional.isPresent()){
            final ImageOffset imageOffset = offsetOptional.get();
            x += imageOffset.getX();
            y += imageOffset.getY();
        }
        batch.draw(textureRegion, x,
                y, textureBounds.getWidth(),
                textureBounds.getHeight());
    }


    protected void updateTextureBounds(ActorConfig config) {
        //get the screen width
        final Rectangle textureBounds = this.getTextureBounds();
        final float bodyWidth = getWidth();
        final float bodyHeight = getHeight();
        textureBounds.x = transformToScreen(getBody().getPosition().x - bodyWidth / 2);
        textureBounds.y = transformToScreen(getBody().getPosition().y - bodyHeight / 2);
        textureBounds.width = transformToScreen(bodyWidth);
        textureBounds.height = transformToScreen(bodyHeight);
    }

    protected float transformToScreen(float n) {
        return n * WORLD_TO_SCREEN;
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

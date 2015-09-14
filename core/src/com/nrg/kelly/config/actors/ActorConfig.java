package com.nrg.kelly.config.actors;

import java.util.List;

/**
 * Created by Andrew on 3/05/2015.
 */
public abstract class ActorConfig {

    private float height;
    private float width;
    private float density;
    private PositionConfig position;
    private float frameRate;
    private float hitBoxScale = 1.0f;
    private HitVectorConfig hitVectorConfig;
    private float gravityScale = 1.0f;

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public HitVectorConfig getHitVectorConfig() {
        return hitVectorConfig;
    }

    public void setHitVectorConfig(HitVectorConfig hitVectorConfig) {
        this.hitVectorConfig = hitVectorConfig;
    }

    private List<AtlasConfig> animations;

    public List<AtlasConfig> getAnimations() {
        return animations;
    }

    public void setAnimations(List<AtlasConfig> animations) {
        this.animations = animations;
    }

    public PositionConfig getPosition() {
        return position;
    }

    public void setPosition(PositionConfig position) {
        this.position = position;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(float frameRate) {
        this.frameRate = frameRate;
    }

    public float getHitBoxScale() {
        return hitBoxScale;
    }

    public void setHitBoxScale(float hitBoxScale) {
        this.hitBoxScale = hitBoxScale;
    }
}

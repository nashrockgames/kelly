package com.nrg.kelly.config.actors;

import java.util.List;

/**
 * Created by Andrew on 3/05/2015.
 */
public abstract class ActorConfig {

    private float height;
    private float width;
    private float density;
    private Position position;

    private List<AtlasConfig> animations;

    public List<AtlasConfig> getAnimations() {
        return animations;
    }

    public void setAnimations(List<AtlasConfig> animations) {
        this.animations = animations;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
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

}

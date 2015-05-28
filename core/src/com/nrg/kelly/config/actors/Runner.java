package com.nrg.kelly.config.actors;

public class Runner extends Actor {

    private float jumpImpulseX;
    private float jumpImpulseY;
    private float gravityScale;
    private float slideX;
    private float slideY;
    private float hitAngularImpulse;
    private String atlas;

    public String getAtlas() {
        return atlas;
    }

    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }

    public float getJumpImpulseX() {
        return jumpImpulseX;
    }

    public void setJumpImpulseX(float jumpImpulseX) {
        this.jumpImpulseX = jumpImpulseX;
    }

    public float getJumpImpulseY() {
        return jumpImpulseY;
    }

    public void setJumpImpulseY(float jumpImpulseY) {
        this.jumpImpulseY = jumpImpulseY;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public float getSlideX() {
        return slideX;
    }

    public float getSlideY() {
        return slideY;
    }

    public float getHitAngularImpulse() {
        return hitAngularImpulse;
    }

    public void setHitAngularImpulse(float hitAngularImpulse) {
        this.hitAngularImpulse = hitAngularImpulse;
    }

    public void setSlideX(float slideX) {
        this.slideX = slideX;
    }

    public void setSlideY(float slideY) {
        this.slideY = slideY;
    }
}
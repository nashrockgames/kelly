package com.nrg.kelly.config.actors;

public class RunnerConfig extends ActorConfig {

    private float jumpImpulseX;
    private float jumpImpulseY;
    private float gravityScale;
    private float hitImpulseX;
    private float hitImpulseY;
    private float hitVelocityX;
    private float hitVelocityY;
    private float hitPauseTime;
    private float slideTime;


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

    public float getHitImpulseX() {
        return hitImpulseX;
    }

    public void setHitImpulseX(float hitImpulseX) {
        this.hitImpulseX = hitImpulseX;
    }

    public float getHitImpulseY() {
        return hitImpulseY;
    }

    public void setHitImpulseY(float hitImpulseY) {
        this.hitImpulseY = hitImpulseY;
    }

    public float getHitVelocityX() {
        return hitVelocityX;
    }

    public void setHitVelocityX(float hitVelocityX) {
        this.hitVelocityX = hitVelocityX;
    }

    public float getHitVelocityY() {
        return hitVelocityY;
    }

    public void setHitVelocityY(float hitVelocityY) {
        this.hitVelocityY = hitVelocityY;
    }

    public float getHitPauseTime() {
        return hitPauseTime;
    }

    public void setHitPauseTime(float hitPauseTime) {
        this.hitPauseTime = hitPauseTime;
    }

    public float getSlideTime() {
        return slideTime;
    }

    public void setSlideTime(float slideTime) {
        this.slideTime = slideTime;
    }
}
package com.nrg.kelly.config.actors;

public class RunnerConfig extends ActorConfig {

    private float jumpImpulseX;
    private float jumpImpulseY;
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

    public float getSlideTime() {
        return slideTime;
    }

    public void setSlideTime(float slideTime) {
        this.slideTime = slideTime;
    }
}
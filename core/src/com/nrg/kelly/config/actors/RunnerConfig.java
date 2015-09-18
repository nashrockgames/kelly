package com.nrg.kelly.config.actors;

public class RunnerConfig extends ActorConfig {

    private float jumpImpulseX;
    private float jumpImpulseY;
    private float slideTime;
    private PositionConfig endOfLevelPosition;
    private float endOfLevelVelocityX;
    private float endOfLevelVelocityY;

    public PositionConfig getEndOfLevelPosition() {
        return endOfLevelPosition;
    }

    public void setEndOfLevelPosition(PositionConfig endOfLevelPosition) {
        this.endOfLevelPosition = endOfLevelPosition;
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

    public float getSlideTime() {
        return slideTime;
    }

    public void setSlideTime(float slideTime) {
        this.slideTime = slideTime;
    }

    public float getEndOfLevelVelocityX() {
        return endOfLevelVelocityX;
    }

    public void setEndOfLevelVelocityX(float endOfLevelVelocityX) {
        this.endOfLevelVelocityX = endOfLevelVelocityX;
    }

    public float getEndOfLevelVelocityY() {
        return endOfLevelVelocityY;
    }

    public void setEndOfLevelVelocityY(float endOfLevelVelocityY) {
        this.endOfLevelVelocityY = endOfLevelVelocityY;
    }
}
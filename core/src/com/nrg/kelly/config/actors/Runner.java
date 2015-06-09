package com.nrg.kelly.config.actors;

public class Runner extends ActorConfig {

    private float jumpImpulseX;
    private float jumpImpulseY;
    private float gravityScale;
    private float hitAngularImpulse;


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


    public float getHitAngularImpulse() {
        return hitAngularImpulse;
    }

    public void setHitAngularImpulse(float hitAngularImpulse) {
        this.hitAngularImpulse = hitAngularImpulse;
    }

}
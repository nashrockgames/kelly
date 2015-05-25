package com.nrg.kelly.config.actors;

import com.nrg.kelly.config.actors.Actor;

/**
 * Created by Andrew on 3/05/2015.
 */
public class Runner extends Actor {

    private float jumpImpulseX;
    private float jumpImpulseY;
    private float gravityScale;
    private float slideX;
    private float slideY;

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
}
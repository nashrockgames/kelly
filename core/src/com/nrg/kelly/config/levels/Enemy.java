package com.nrg.kelly.config.levels;

import com.nrg.kelly.config.actors.ActorConfig;

/**
 * Created by Andrew on 27/05/2015.
 */
public class Enemy extends ActorConfig{

    private float velocityX;

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }
}

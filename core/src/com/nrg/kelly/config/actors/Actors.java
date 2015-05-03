package com.nrg.kelly.config.actors;

/**
 * Created by Andrew on 3/05/2015.
 */
public class Actors {

    private Ground ground;

    private WorldGravity worldGravity;

    private Runner runner;

    public Ground getGround() {
        return ground;
    }

    public void setGround(Ground ground) {
        this.ground = ground;
    }

    public Runner getRunner() {
        return runner;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public WorldGravity getWorldGravity() {
        return worldGravity;
    }

    public void setWorldGravity(WorldGravity worldGravity) {
        this.worldGravity = worldGravity;
    }

}

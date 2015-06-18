package com.nrg.kelly.config.actors;

import com.nrg.kelly.config.buttons.PlayButtonConfig;

/**
 * Created by Andrew on 3/05/2015.
 */
public class Actors {

    private Ground ground;

    private WorldGravity worldGravity;

    private Runner runner;

    private PlayButtonConfig playButton;

    public PlayButtonConfig getPlayButton() {
        return playButton;
    }

    public void setPlayButton(PlayButtonConfig playButton) {
        this.playButton = playButton;
    }

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

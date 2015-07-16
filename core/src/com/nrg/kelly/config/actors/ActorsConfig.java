package com.nrg.kelly.config.actors;

import com.nrg.kelly.config.buttons.PlayButtonConfig;

/**
 * Created by Andrew on 3/05/2015.
 */
public class ActorsConfig {

    private GroundConfig ground;

    private WorldGravityConfig worldGravity;

    private RunnerConfig runner;

    private PlayButtonConfig playButton;

    private ArmourConfig armour;

    public ArmourConfig getArmour() {
        return armour;
    }

    public void setArmour(ArmourConfig armour) {
        this.armour = armour;
    }

    public PlayButtonConfig getPlayButton() {
        return playButton;
    }

    public void setPlayButton(PlayButtonConfig playButton) {
        this.playButton = playButton;
    }

    public GroundConfig getGround() {
        return ground;
    }

    public void setGround(GroundConfig ground) {
        this.ground = ground;
    }

    public RunnerConfig getRunner() {
        return runner;
    }

    public void setRunner(RunnerConfig runnerConfig) {
        this.runner = runnerConfig;
    }

    public WorldGravityConfig getWorldGravity() {
        return worldGravity;
    }

    public void setWorldGravity(WorldGravityConfig worldGravity) {
        this.worldGravity = worldGravity;
    }

}

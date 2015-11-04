package com.nrg.kelly.config.actors;

import com.nrg.kelly.config.buttons.HighScoreButtonConfig;
import com.nrg.kelly.config.buttons.PlayButtonConfig;

/**
 * Created by Andrew on 3/05/2015.
 */
public class ActorsConfig {

    private GroundConfig ground;

    private WorldGravityConfig worldGravity;

    private RunnerConfig runner;

    private RunnerBulletConfig runnerBullet;

    private PlayButtonConfig playButton;

    private HighScoreButtonConfig highScoreButton;

    private ArmourConfig armour;

    private GunConfig gun;

    public HighScoreButtonConfig getHighScoreButton() {
        return highScoreButton;
    }

    public void setHighScoreButton(HighScoreButtonConfig highScoreButton) {
        this.highScoreButton = highScoreButton;
    }

    public GunConfig getGun() {
        return gun;
    }

    public void setGun(GunConfig gun) {
        this.gun = gun;
    }

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

    public RunnerBulletConfig getRunnerBullet() {
        return runnerBullet;
    }

    public void setRunnerBullet(RunnerBulletConfig runnerBullet) {
        this.runnerBullet = runnerBullet;
    }
}

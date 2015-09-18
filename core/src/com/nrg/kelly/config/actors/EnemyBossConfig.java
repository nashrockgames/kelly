package com.nrg.kelly.config.actors;

/**
 * Created by Andrew on 16/07/2015.
 */
public class EnemyBossConfig extends EnemyConfig {

    private int armourSpawnInterval;
    private int gunSpawnInterval;

    private PositionConfig endOfLevelPosition;
    private float endOfLevelVelocityX;
    private float endOfLevelVelocityY;

    public PositionConfig getEndOfLevelPosition() {
        return endOfLevelPosition;
    }

    public void setEndOfLevelPosition(PositionConfig endOfLevelPosition) {
        this.endOfLevelPosition = endOfLevelPosition;
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

    public int getArmourSpawnInterval() {
        return armourSpawnInterval;
    }

    public void setArmourSpawnInterval(int armourSpawnInterval) {
        this.armourSpawnInterval = armourSpawnInterval;
    }

    public int getGunSpawnInterval() {
        return gunSpawnInterval;
    }

    public void setGunSpawnInterval(int gunSpawnInterval) {
        this.gunSpawnInterval = gunSpawnInterval;
    }
}

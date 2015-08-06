package com.nrg.kelly.config.actors;

/**
 * Created by Andrew on 16/07/2015.
 */
public class EnemyBossConfig extends EnemyConfig {

    private int armourSpawnInterval;
    private int gunSpawnInterval;

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

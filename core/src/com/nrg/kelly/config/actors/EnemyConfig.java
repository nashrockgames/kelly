package com.nrg.kelly.config.actors;

public class EnemyConfig extends ActorConfig{

    private float velocityX;
    private float armourHitVelocityX;
    private float armourHitVelocityY;
    private float armourHitImpulseX;
    private float armourHitImpulseY;
    private float armourHitRotationDelta;
    private int armourSpawnInterval;

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getArmourHitVelocityX() {
        return armourHitVelocityX;
    }

    public void setArmourHitVelocityX(float armourHitVelocityX) {
        this.armourHitVelocityX = armourHitVelocityX;
    }

    public float getArmourHitVelocityY() {
        return armourHitVelocityY;
    }

    public void setArmourHitVelocityY(float armourHitVelocityY) {
        this.armourHitVelocityY = armourHitVelocityY;
    }

    public float getArmourHitImpulseX() {
        return armourHitImpulseX;
    }

    public void setArmourHitImpulseX(float armourHitImpulseX) {
        this.armourHitImpulseX = armourHitImpulseX;
    }

    public float getArmourHitImpulseY() {
        return armourHitImpulseY;
    }

    public void setArmourHitImpulseY(float armourHitImpulseY) {
        this.armourHitImpulseY = armourHitImpulseY;
    }

    public float getArmourHitRotationDelta() {
        return armourHitRotationDelta;
    }

    public void setArmourHitRotationDelta(float armourHitRotationDelta) {
        this.armourHitRotationDelta = armourHitRotationDelta;
    }

    public int getArmourSpawnInterval() {
        return armourSpawnInterval;
    }

    public void setArmourSpawnInterval(int armourSpawnInterval) {
        this.armourSpawnInterval = armourSpawnInterval;
    }
}

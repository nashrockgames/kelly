package com.nrg.kelly.config;

import com.nrg.kelly.config.actors.ActorsConfig;
import com.nrg.kelly.config.settings.Settings;

/**
 * Created by Andrew on 3/05/2015.
 */
public class GameConfig {

    private Settings settings;

    private ActorsConfig actors;

    private float viewPortWidth;

    private float viewPortHeight;

    private int levelCount;

    private int worldVelocityIterations;

    private int worldPositionIterations;
    private float worldTimeStepDenominator;
    private float enemySpawnDelaySeconds;
    private float reduceEnemySpawnIntervalSeconds;
    private float reduceEnemySpawnDelayPercentage;
    private int spawnBossOnEnemyCount;

    public GameConfig(){
    }

    public int getWorldPositionIterations() {
        return worldPositionIterations;
    }

    public void setWorldPositionIterations(int worldPositionIterations) {
        this.worldPositionIterations = worldPositionIterations;
    }

    public int getWorldVelocityIterations() {
        return worldVelocityIterations;
    }

    public void setWorldVelocityIterations(int worldVelocityIterations) {
        this.worldVelocityIterations = worldVelocityIterations;
    }

    public float getViewPortWidth() {
        return viewPortWidth;
    }

    public void setViewPortWidth(float viewPortWidth) {
        this.viewPortWidth = viewPortWidth;
    }

    public float getViewPortHeight() {
        return viewPortHeight;
    }

    public void setViewPortHeight(float viewPortHeight) {
        this.viewPortHeight = viewPortHeight;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public ActorsConfig getActors() {
        return actors;
    }

    public void setActors(ActorsConfig actors) {
        this.actors = actors;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }

    public float getWorldTimeStepDenominator() {
        return worldTimeStepDenominator;
    }

    public void setWorldTimeStepDenominator(float worldTimeStepDenominator) {
        this.worldTimeStepDenominator = worldTimeStepDenominator;
    }

    public float getEnemySpawnDelaySeconds() {
        return enemySpawnDelaySeconds;
    }

    public void setEnemySpawnDelaySeconds(float enemySpawnDelaySeconds) {
        this.enemySpawnDelaySeconds = enemySpawnDelaySeconds;
    }

    public float getReduceEnemySpawnIntervalSeconds() {
        return reduceEnemySpawnIntervalSeconds;
    }

    public void setReduceEnemySpawnIntervalSeconds(float reduceEnemySpawnIntervalSeconds) {
        this.reduceEnemySpawnIntervalSeconds = reduceEnemySpawnIntervalSeconds;
    }

    public float getReduceEnemySpawnDelayPercentage() {
        return reduceEnemySpawnDelayPercentage;
    }

    public void setReduceEnemySpawnDelayPercentage(float reduceEnemySpawnDelayPercentage) {
        this.reduceEnemySpawnDelayPercentage = reduceEnemySpawnDelayPercentage;
    }

    public int getSpawnBossOnEnemyCount() {
        return spawnBossOnEnemyCount;
    }

    public void setSpawnBossOnEnemyCount(int spawnBossOnEnemyCount) {
        this.spawnBossOnEnemyCount = spawnBossOnEnemyCount;
    }
}

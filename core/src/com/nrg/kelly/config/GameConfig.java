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

    public GameConfig(){
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
}

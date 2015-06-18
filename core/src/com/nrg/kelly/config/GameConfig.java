package com.nrg.kelly.config;

import com.nrg.kelly.config.actors.Actors;
import com.nrg.kelly.config.actors.WorldGravity;
import com.nrg.kelly.config.settings.Settings;

import java.util.List;

/**
 * Created by Andrew on 3/05/2015.
 */
public class GameConfig {

    private Settings settings;

    private Actors actors;

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

    public Actors getActors() {
        return actors;
    }

    public void setActors(Actors actors) {
        this.actors = actors;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }
}

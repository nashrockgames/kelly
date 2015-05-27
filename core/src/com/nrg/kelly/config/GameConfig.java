package com.nrg.kelly.config;

import com.nrg.kelly.config.actors.Actors;
import com.nrg.kelly.config.actors.WorldGravity;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.config.settings.Settings;

import java.util.List;

/**
 * Created by Andrew on 3/05/2015.
 */
public class GameConfig {

    private Settings settings;

    private Actors actors;

    private List<Menu> menus;

    private int levelCount;

    public GameConfig(){
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

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public int getLevelCount() {
        return levelCount;
    }

    public void setLevelCount(int levelCount) {
        this.levelCount = levelCount;
    }
}

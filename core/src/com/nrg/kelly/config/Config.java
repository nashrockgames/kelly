package com.nrg.kelly.config;

import com.nrg.kelly.config.actors.Actors;
import com.nrg.kelly.config.actors.WorldGravity;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.config.settings.Settings;

import java.util.List;

/**
 * Created by Andrew on 3/05/2015.
 */
public class Config {

    private Settings settings;

    private Actors actors;

    private List<Menu> menus;

    public Config(){

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
}

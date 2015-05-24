package com.nrg.kelly.config.menus;

import com.nrg.kelly.config.menus.buttons.Button;

import java.util.List;

/**
 * Created by Andrew on 12/05/2015.
 */
public class Menu {

    private String id;
    private List<Button> buttons;
    private String skin;
    private String atlas;

    public Menu(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public String getSkin() {
        return skin;
    }
    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getAtlas() {
        return atlas;
    }

    public void setAtlas(String atlas) {
        this.atlas = atlas;
    }
}

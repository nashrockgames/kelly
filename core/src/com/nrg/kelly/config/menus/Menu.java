package com.nrg.kelly.config.menus;

import com.nrg.kelly.config.menus.buttons.Button;

import java.util.List;

/**
 * Created by Andrew on 12/05/2015.
 */
public class Menu {

    private String id;

    private List<Button> buttons;

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
}

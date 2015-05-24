package com.nrg.kelly.config.factories;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nrg.kelly.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew on 14/05/2015.
 */
public class MenuButtonFactory{

    private static MenuButtonFactory menuButtonFactory;

    private Map<String, Map<String,TextButton>> buttons =
            new LinkedHashMap<String,Map<String,TextButton>>();

    private MenuButtonFactory(){

    }

    public static MenuButtonFactory getInstance(){
        if(menuButtonFactory==null){
            menuButtonFactory = new MenuButtonFactory();
        }
        return menuButtonFactory;
    }

    public Map<String, TextButton> getMenuButtons(Constants.MENU_ID menuId){
        if(this.buttons.containsKey(menuId.toString())){
            return buttons.get(menuId.toString());
        }
        throw new IllegalArgumentException("Unable to find menu id " + menuId.toString());
    }

    protected Map<String, Map<String,TextButton>> getMenuButtons(){
        return this.buttons;
    }

    public boolean isConfigured() {
        return !menuButtonFactory.buttons.isEmpty();
    }
}

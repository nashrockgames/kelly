package com.nrg.kelly.screens.menus.main;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Andrew on 14/05/2015.
 */
public class MainMenuScreenModel {

    private Map<String,TextButton> textButtons = new LinkedHashMap<String,TextButton>();

    public Map<String,TextButton> getTextButtons() {
        return textButtons;
    }
}

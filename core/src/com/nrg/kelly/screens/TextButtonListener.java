package com.nrg.kelly.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nrg.kelly.Constants;
import com.nrg.kelly.events.menus.ButtonClickedEvent;
import com.nrg.kelly.events.Events;

public class TextButtonListener extends ClickListener {

    private final String id;

    public TextButtonListener(String id){
        this.id = id;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        final Constants.BUTTON_ID menuId = Constants.BUTTON_ID.valueOf(id.toUpperCase());
        Events.get().post(
            new ButtonClickedEvent(menuId)
        );
    }
}


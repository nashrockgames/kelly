package com.nrg.kelly.screens;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nrg.kelly.Constants;
import com.nrg.kelly.events.ButtonClickedEvent;
import com.nrg.kelly.events.Events;

/**
 * Created by Andrew on 21/05/2015.
 */
public class TextButtonListener extends ClickListener {

    private final String id;

    public TextButtonListener(String id){
        this.id = id;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        Events.get().post(
            new ButtonClickedEvent(Constants.BUTTON_ID.valueOf(id.toUpperCase()))
        );
    }
}


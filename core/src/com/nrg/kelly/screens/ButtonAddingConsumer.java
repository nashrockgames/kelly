package com.nrg.kelly.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.nrg.kelly.Constants;
import com.nrg.kelly.events.ButtonClickedEvent;
import com.nrg.kelly.events.Events;

import java.util.function.BiConsumer;

/**
 * Created by Andrew on 21/05/2015.
 */
public class ButtonAddingConsumer implements BiConsumer<String, TextButton>{

    private final Optional<Table> table;

    public ButtonAddingConsumer(Optional<Table> table){
        this.table = table;
    }

    @Override
    public void accept(final String id, final TextButton textButton) {
        textButton.addListener(new TextButtonListener(id));
        if(table.isPresent()) {
            table.get().add(textButton).row();
        }
    }
}

package com.nrg.kelly.screens.menus.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.google.common.base.Optional;
import com.nrg.kelly.screens.ButtonAddingConsumer;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Andrew on 11/05/2015.
 */
public class MainMenuScreenView {

    @Inject
    MainMenuScreenModel mainMenuScreenModel;

    final Table table = new Table();

    final Stage stage = new Stage();

    @Inject
    public MainMenuScreenView(){

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        this.stage.draw();
        this.stage.act(delta);
    }

    public void show(){
        final Map<String,TextButton> textButtons = mainMenuScreenModel.getTextButtons();
        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight());
        final ButtonAddingConsumer buttonAddingConsumer = new ButtonAddingConsumer(Optional.of(table));
        for(String id : textButtons.keySet()) {
            buttonAddingConsumer.accept(id, textButtons.get(id));
        }
        this.table.setFillParent(true);
        this.stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }



}

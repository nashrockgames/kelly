package com.nrg.kelly.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.nrg.kelly.stages.GameStageView;

import javax.inject.Inject;

public class GameScreenView {

    @Inject
    GameStageView gameStageView;

    @Inject
    public GameScreenView(){

    }

    public void show() {
        Gdx.input.setInputProcessor(gameStageView);
        gameStageView.show();
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStageView.draw();
        gameStageView.act(delta);
    }
}

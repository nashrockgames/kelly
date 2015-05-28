package com.nrg.kelly.screens.game;

import com.badlogic.gdx.Gdx;
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
        gameStageView.draw();
        gameStageView.act(delta);
    }
}

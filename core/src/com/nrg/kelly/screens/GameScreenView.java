package com.nrg.kelly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.nrg.kelly.stages.Box2dGameStage;

import javax.inject.Inject;

/**
 * Created by Andrew on 11/05/2015.
 */
public class GameScreenView {

    @Inject
    Box2dGameStage box2dGameStage;

    @Inject
    public GameScreenView(){

    }

    public void show() {
        box2dGameStage.show();
    }

    public void render(float delta) {
        box2dGameStage.draw();
        box2dGameStage.act(delta);
    }
}

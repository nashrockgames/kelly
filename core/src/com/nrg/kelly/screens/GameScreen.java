package com.nrg.kelly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.nrg.kelly.stages.Box2dGameStage;


import javax.inject.Inject;

/**
 * Created by Andrew on 26/04/2015.
 */
public class GameScreen implements Screen {

    @Inject
    GameScreenView gameScreenView;

    @Inject
    public GameScreen(){

    }

    @Override
    public void show() {
        gameScreenView.show();
    }

    @Override
    public void render(float delta) {

        gameScreenView.render(delta);

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
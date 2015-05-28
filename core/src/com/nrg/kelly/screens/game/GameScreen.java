package com.nrg.kelly.screens.game;

import com.badlogic.gdx.Screen;

import javax.inject.Inject;

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
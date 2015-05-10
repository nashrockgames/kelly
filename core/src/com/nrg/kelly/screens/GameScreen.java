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
    Box2dGameStage box2dGameStage;

    @Inject
    public GameScreen(){

    }

    @Override
    public void show() {
        box2dGameStage.show();
    }

    @Override
    public void render(float delta) {

        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Update the stage
        box2dGameStage.draw();
        box2dGameStage.act(delta);
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
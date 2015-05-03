package com.nrg.kelly.stages.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.inject.Inject;

/**
 * Created by Andrew on 3/05/2015.
 */
public class DebugText extends TextActor {

    @Inject
    public DebugText(){
        this.getFont().setColor(Color.BLUE);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        getFont().draw(batch, this.getText(), 0, 0);
        batch.end();
    }
}

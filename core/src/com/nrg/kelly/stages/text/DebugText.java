package com.nrg.kelly.stages.text;

import com.badlogic.gdx.graphics.g2d.Batch;

import javax.inject.Inject;

/**
 * Created by Andrew on 3/05/2015.
 */
public class DebugText extends TextActor {

    @Inject
    public DebugText(){
        this.setX(1.0f);
        this.setY(1.0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        getFont().draw(batch, this.getText(), 1.0f, 0.5f);
    }
}

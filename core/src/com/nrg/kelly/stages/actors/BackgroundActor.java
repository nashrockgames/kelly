package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.nrg.kelly.Constants;

public class BackgroundActor extends ScrollingActor {

    public BackgroundActor(String imagePath) {
        super(null, imagePath);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x,
                textureRegionBounds1.y, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        batch.draw(textureRegion, textureRegionBounds2.x,
                textureRegionBounds2.y, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
    }

}

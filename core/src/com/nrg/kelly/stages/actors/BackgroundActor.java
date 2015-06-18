package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.nrg.kelly.config.CameraConfig;

public class BackgroundActor extends ScrollingActor {

    public BackgroundActor(String imagePath, CameraConfig cameraConfig) {
        super(null, imagePath, cameraConfig);
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

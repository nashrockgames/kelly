package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.nrg.kelly.Constants;

public class BackgroundActor extends ScrollingActor {

    public BackgroundActor(String imagePath) {
        super(imagePath);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureRegionBounds1.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
        batch.draw(textureRegion, textureRegionBounds2.x, textureRegionBounds2.y, Constants.APP_WIDTH,
                Constants.APP_HEIGHT);
    }
}

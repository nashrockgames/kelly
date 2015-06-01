package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.nrg.kelly.Constants;
import com.nrg.kelly.physics.Box2dFactory;

/**
 * Created by Andrew on 2/05/2015.
 */
public class GroundActor extends ScrollingActor {

    public GroundActor(String imagePath) {
        super(imagePath);
        final Body ground = Box2dFactory.getInstance().createGround();
        ground.setUserData(this);
        this.setWidth(Constants.APP_WIDTH);
        this.setHeight(2f);
        setBody(ground);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(textureRegion, textureRegionBounds1.x, textureBounds.y, textureBounds.getWidth(),
                textureBounds.getHeight());
        batch.draw(textureRegion, textureRegionBounds2.x, textureBounds.y, textureBounds.getWidth(),
                textureBounds.getHeight());
    }

}
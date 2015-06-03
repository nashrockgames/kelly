package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.base.Optional;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.Ground;
import com.nrg.kelly.physics.Box2dFactory;

/**
 * Created by Andrew on 2/05/2015.
 */
public class GroundActor extends ScrollingActor {

    public GroundActor(Ground config, String imagePath) {
        super(config, imagePath);
        final Body ground = Box2dFactory.getInstance().createGround();
        ground.setUserData(this);
        this.setWidth(config.getWidth());
        this.setHeight(config.getHeight());
        setBody(ground);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        final Rectangle textureBounds = this.getTextureBounds();
        batch.draw(textureRegion, textureRegionBounds1.x, textureBounds.y, textureBounds.getWidth(),
                textureBounds.getHeight());
        batch.draw(textureRegion, textureRegionBounds2.x, textureBounds.y, textureBounds.getWidth(),
                textureBounds.getHeight());
    }
}
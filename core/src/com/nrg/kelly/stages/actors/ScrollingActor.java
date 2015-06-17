package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.nrg.kelly.config.actors.ActorConfig;

public abstract class ScrollingActor extends GameActor {

    protected final TextureRegion textureRegion;
    protected Rectangle textureRegionBounds1;
    protected Rectangle textureRegionBounds2;
    private int speed = 200;

    public ScrollingActor(ActorConfig config, String imagePath) {
        super(config);
        textureRegion = new TextureRegion(new Texture(Gdx.files.internal(imagePath)));
        textureRegionBounds1 = new Rectangle(0 - Gdx.graphics.getWidth() / 2, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth() / 2, 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (leftBoundsReached(delta)) {
            resetBounds();
        } else {
            updateXBounds(-delta);
        }
    }

    private boolean leftBoundsReached(float delta) {
        return (textureRegionBounds2.x - (delta * speed)) <= 0;
    }

    private void updateXBounds(float delta) {
        textureRegionBounds1.x += delta * speed;
        textureRegionBounds2.x += delta * speed;
    }

    private void resetBounds() {
        textureRegionBounds1 = textureRegionBounds2;
        textureRegionBounds2 = new Rectangle(Gdx.graphics.getWidth(), 0,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

}

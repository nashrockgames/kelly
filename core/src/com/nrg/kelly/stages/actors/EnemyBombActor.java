package com.nrg.kelly.stages.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.BossBombConfig;

import java.util.List;


public class EnemyBombActor extends EnemyActor {

    private final Animation explodeAnimation;
    private final AtlasConfig explodeAtlasConfig;
    private float explosionPositionX;

    private boolean exploded = false;

    public EnemyBombActor(BossBombConfig bossBombConfig, CameraConfig cameraConfig) {
        super(bossBombConfig, cameraConfig);
        final List<AtlasConfig> atlasConfigList = bossBombConfig.getAnimations();
        explodeAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "explosion");
        final String explode = explodeAtlasConfig.getAtlas();
        final TextureAtlas explodeAtlas = new TextureAtlas(Gdx.files.internal(explode));
        explodeAnimation = new Animation(bossBombConfig.getFrameRate(), explodeAtlas.getRegions());

    }

    public void explode(){
        this.exploded = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        if(exploded) {
            this.setDefaultAnimation(explodeAnimation);
        }
        this.drawDefaultAnimation(batch);

    }

    @Override
    public void act(float delta) {

        super.act(delta);

        if(this.getBody().getPosition().x  <= this.getExplosionPositionX() ){
            if(!exploded)
            this.explode();
        }

    }

    public float getExplosionPositionX() {
        return explosionPositionX;
    }

    public void setExplosionPositionX(float explosionPositionX) {
        this.explosionPositionX = explosionPositionX;
    }
}

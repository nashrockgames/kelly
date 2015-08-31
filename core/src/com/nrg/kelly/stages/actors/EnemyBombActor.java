package com.nrg.kelly.stages.actors;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.BossBombConfig;
import com.nrg.kelly.physics.Box2dFactory;

import java.util.List;


public class EnemyBombActor extends EnemyActor {

    private final Animation explodeAnimation;
    private final AtlasConfig explodeAtlasConfig;
    private float explosionPositionX;

    private boolean exploded = false;
    private boolean dead = false;

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
        /*
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                final Body body = getBody();
                if(body!=null) {
                    body.setUserData(null);
                    Box2dFactory.destroyBody(body);
                }
                dead = true;
            }
        },0.35f);
        */
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
        if(this.dead){
            remove();
        }

    }

    public float getExplosionPositionX() {
        return explosionPositionX;
    }

    public void setExplosionPositionX(float explosionPositionX) {
        this.explosionPositionX = explosionPositionX;
    }
}

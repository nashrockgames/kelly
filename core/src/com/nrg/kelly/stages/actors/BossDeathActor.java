package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.google.common.base.Optional;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.BossDeathConfig;
import com.nrg.kelly.config.actors.HitVectorConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;

/**
 * Created by Andrew on 9/09/2015.
 */
public class BossDeathActor extends GameActor {

    private boolean impulseApplied = false;

    public BossDeathActor(BossDeathConfig bossDeathConfig, CameraConfig cameraConfig) {
        super(bossDeathConfig, cameraConfig);
        final Body body = Box2dFactory.getInstance().createBossDeath(bossDeathConfig);
        body.setUserData(this);
        setBody(body);
        setWidth(bossDeathConfig.getWidth());
        setHeight(bossDeathConfig.getHeight());
        setActorState(ActorState.FALLING);
        Events.get().register(this);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        this.drawDefaultAnimation(batch);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        for(ActorConfig actorConfig : this.getConfig().asSet()) {
            if(!impulseApplied){
                final HitVectorConfig hitVectorConfig = actorConfig.getHitVectorConfig();
                final float hitVelocityX = hitVectorConfig.getHitVelocityX();
                final float hitVelocityY = hitVectorConfig.getHitVelocityY();
                final float hitImpulseX = hitVectorConfig.getHitImpulseX();
                final float hitImpulseY = hitVectorConfig.getHitImpulseY();
                final Filter f = createDeathCollisionFilter();
                final Vector2 linearVelocity = new Vector2(hitVelocityX, hitVelocityY);
                final Vector2 impulseVector = new Vector2(hitImpulseX, hitImpulseY);
                final Optional<Float> rotationOptional = Optional.absent();
                final CollisionParams collisionParams = new CollisionParams(
                        linearVelocity, impulseVector, f, this.getBody(), rotationOptional);
                applyCollisionImpulse(collisionParams);
                this.impulseApplied = true;
            }
            if (!isWithinBounds(actorConfig)) {
                Box2dFactory.destroyAndRemove(this);
            }
        }
    }

    private Filter createDeathCollisionFilter() {
        final Filter f = new Filter();
        f.categoryBits = Constants.RUNNER_HIT_CATEGORY;
        f.groupIndex = Constants.RUNNER_HIT_GROUP_INDEX;
        f.maskBits = Constants.RUNNER_HIT_MASK_INDEX;
        return f;
    }




}

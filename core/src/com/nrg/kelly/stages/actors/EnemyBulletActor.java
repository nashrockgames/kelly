package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.BossBulletConfig;
import com.nrg.kelly.physics.Box2dFactory;


public class EnemyBulletActor extends EnemyActor implements RayCastCallback {

    private CollisionParams collisionParams;


    public EnemyBulletActor(BossBulletConfig bossBulletConfig, CameraConfig cameraConfig) {
        super(bossBulletConfig, cameraConfig);
        setUpCollisionVectors(bossBulletConfig, this.getBody());
    }


    private void setUpCollisionVectors(BossBulletConfig enemyConfig, Body body) {
        final float armourHitVelocityX = enemyConfig.getArmourHitVelocityX();
        final float armourHitVelocityY = enemyConfig.getArmourHitVelocityY();
        final float armourHitImpulseX = enemyConfig.getArmourHitImpulseX();
        final float armourHitImpulseY = enemyConfig.getArmourHitImpulseY();
        final float armourHitRotationDelta = enemyConfig.getArmourHitRotationDelta();
        final Vector2 armourHitLinearVelocityX =
                new Vector2(armourHitVelocityX, armourHitVelocityY);
        final Vector2 armourHitLinearVelocityY =
                new Vector2(armourHitImpulseX, armourHitImpulseY);
        Optional<Float> rotationOptional = Optional.of(armourHitRotationDelta);
        final Filter armourCollisionFilter = createArmourCollisionFilter();
        collisionParams = new CollisionParams(armourHitLinearVelocityX,
                armourHitLinearVelocityY, armourCollisionFilter, body, rotationOptional);

    }


    @Override
    public void act(float delta) {

        super.act(delta);
        final ActorState actorState = this.getActorState();
        if(actorState.equals(ActorState.HIT_BY_ARMOUR)||
                actorState.equals(ActorState.FALLING)) {
            for(ActorConfig actorConfig : this.getActorConfigOptional().asSet()) {
                this.applyCollisionImpulse(collisionParams);
                if (!isWithinBounds(actorConfig)) {
                    Box2dFactory.destroyAndRemove(this);
                }
            }
        } else {
            this.checkRayCast();
        }
    }
    public void checkRayCast(){

        final Vector2 position = this.getBody().getPosition();
        final Vector2 rayCastVector = new Vector2(position.x - 1.0f, position.y);
        Box2dFactory.getWorld().rayCast(this , position, rayCastVector);

    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

        final Object userData = fixture.getBody().getUserData();
        if(userData instanceof RunnerActor){
            RunnerActor runnerActor = (RunnerActor)userData;
            final AnimationState animationState = runnerActor.getAnimationState();
            if(animationState.equals(AnimationState.ARMOUR_EQUIPPED)
                    ||animationState.equals(AnimationState.ARMOUR_AND_GUN_EQUIPPED)) {
                this.setActorState(ActorState.HIT_BY_ARMOUR);
            }
            return 0;
        }
        return 1;
    }


}

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
import com.nrg.kelly.config.actors.EnemyBossConfig;
import com.nrg.kelly.config.actors.EnemyConfig;
import com.nrg.kelly.physics.Box2dFactory;


public class EnemyBulletActor extends EnemyActor implements RayCastCallback {

    private float armourHitVelocityX;
    private float armourHitVelocityY;
    private float armourHitImpulseX;
    private float armourHitImpulseY;
    private float armourHitRotationDelta;
    private Vector2 armourHitLinearVelocityX;
    private Vector2 armourHitLinearVelocityY;
    private Optional<Float> rotationOptional;
    private CollisionParams collisionParams;
    private int armourSpawnInterval;


    public EnemyBulletActor(BossBulletConfig bossBulletConfig, CameraConfig cameraConfig) {
        super(bossBulletConfig, cameraConfig);
        setUpCollisionVectors(bossBulletConfig, this.getBody());
        setArmourSpawnInterval(bossBulletConfig.getArmourSpawnInterval());
    }


    private void setUpCollisionVectors(BossBulletConfig enemyConfig, Body body) {
        this.armourHitVelocityX = enemyConfig.getArmourHitVelocityX();
        this.armourHitVelocityY = enemyConfig.getArmourHitVelocityY();
        this.armourHitImpulseX = enemyConfig.getArmourHitImpulseX();
        this.armourHitImpulseY = enemyConfig.getArmourHitImpulseY();
        this.armourHitRotationDelta = enemyConfig.getArmourHitRotationDelta();
        this.armourHitLinearVelocityX = new Vector2(armourHitVelocityX, armourHitVelocityY);
        this.armourHitLinearVelocityY = new Vector2(armourHitImpulseX, armourHitImpulseY);
        this.rotationOptional = Optional.of(armourHitRotationDelta);
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
            for(ActorConfig actorConfig : this.getConfig().asSet()) {
                this.applyCollisionImpulse(collisionParams);
                if (!isWithinBounds(actorConfig)) {
                    final Body body = this.getBody();
                    Box2dFactory.destroyBody(body);
                    this.remove();
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
            if(runnerActor.getAnimationState().equals(AnimationState.ARMOUR_EQUIPPED)) {
                this.setActorState(ActorState.HIT_BY_ARMOUR);
            }
            return 0;
        }
        return 1;
    }

    public int getArmourSpawnInterval() {
        return armourSpawnInterval;
    }

    public void setArmourSpawnInterval(int armourSpawnInterval) {
        this.armourSpawnInterval = armourSpawnInterval;
    }

}

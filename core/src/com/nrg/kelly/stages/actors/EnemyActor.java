package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.EnemyConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.physics.Box2dFactory;

public class EnemyActor extends GameActor {

    protected Vector2 configuredLinearVelocity;

    public boolean runnerHit = false;
    private Optional<RunnerActor> runnerActorOptional = Optional.absent();

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

    public EnemyActor(EnemyConfig enemyConfig, CameraConfig cameraConfig) {
        super(enemyConfig, cameraConfig);
        final Body body = Box2dFactory.getInstance().createEnemy(enemyConfig);
        body.setUserData(this);
        setBody(body);
        setWidth(enemyConfig.getWidth());
        setHeight(enemyConfig.getHeight());
        setUpCollisionVectors(enemyConfig, body);
        setArmourSpawnInterval(enemyConfig.getArmourSpawnInterval());
        Events.get().register(this);
    }

    private void setUpCollisionVectors(EnemyConfig enemyConfig, Body body) {
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

    public void setConfiguredLinearVelocity(Vector2 configuredLinearVelocity){
        this.configuredLinearVelocity = configuredLinearVelocity;
    }

    @Subscribe
    public void runnerHit(RunnerHitEvent runnerHitEvent){
        runnerActorOptional = Optional.of(runnerHitEvent.getRunnerActor());
        this.runnerHit = true;
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        final Body body = this.getBody();
        final ActorState actorState = this.getActorState();
        if(actorState.equals(ActorState.HIT_BY_ARMOUR)||
                actorState.equals(ActorState.FALLING)) {
            for(ActorConfig actorConfig : this.getConfig().asSet()) {
                this.applyCollisionImpulse(collisionParams);
                if (!isWithinBounds(body, actorConfig)) {
                    Box2dFactory.destroyBody(body);
                    this.remove();
                }
            }
        }else{
            body.setLinearVelocity(configuredLinearVelocity);
            for (ActorConfig actorConfig : this.getConfig().asSet()) {
                if (!(body.getPosition().x + actorConfig.getWidth() / 2 > 0)) {
                    if (!runnerHit) {
                        Box2dFactory.destroyBody(body);
                        this.remove();
                    }
                }
            }
        }
    }

    private boolean isWithinBounds(Body body, ActorConfig actorConfig) {
            final CameraConfig cameraConfig = this.getCameraConfig();
            return isWithinWidth(actorConfig, cameraConfig, body) &&
                    isWithinHeight(actorConfig, cameraConfig, body);
    }

    private boolean isWithinHeight(ActorConfig actorConfig, CameraConfig cameraConfig, Body body){

        final Vector2 position = body.getPosition();
        final float y = position.y + (actorConfig.getHeight() / 2.0f);
        return y > 0 && y < cameraConfig.getViewportHeight();

    }

    private boolean isWithinWidth(ActorConfig actorConfig, CameraConfig cameraConfig, Body body){

        final Vector2 position = body.getPosition();
        final float x = position.x + (actorConfig.getWidth() / 2.0f);
        return x > 0 && x < cameraConfig.getViewportWidth();

    }

    protected Filter createArmourCollisionFilter() {
        final Filter f = new Filter();
        f.categoryBits = Constants.ENEMY_ARMOUR_HIT_CATEGORY;
        f.groupIndex = Constants.ENEMY_ARMOUR_HIT_GROUP_INDEX;
        f.maskBits = Constants.ENEMY_ARMOUR_HIT_MASK_INDEX;
        return f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final boolean hasArmour = hasRunnerArmour();
        if(!hasArmour && runnerHit) {
            this.maintainPosition();
            this.drawFirstFrame(batch);
        } else {
            this.drawDefaultAnimation(batch);
        }
    }

    private boolean hasRunnerArmour() {
        for(RunnerActor runner : runnerActorOptional.asSet()){
            if(runner.getAnimationState().equals(AnimationState.ARMOUR_EQUIPPED)){
                return true;
            }
        }
        return false;
    }


    public int getArmourSpawnInterval() {
        return armourSpawnInterval;
    }

    public void setArmourSpawnInterval(int armourSpawnInterval) {
        this.armourSpawnInterval = armourSpawnInterval;
    }
}

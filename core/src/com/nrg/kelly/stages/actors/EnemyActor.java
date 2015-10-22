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

    protected Optional<Vector2> configuredLinearVelocity = Optional.absent();

    private Optional<RunnerActor> runnerActorOptional = Optional.absent();

    public EnemyActor(EnemyConfig enemyConfig, CameraConfig cameraConfig) {
        super(enemyConfig, cameraConfig);
        final Body body = Box2dFactory.createEnemy(enemyConfig);
        body.setUserData(this);
        setBody(body);
        setWidth(enemyConfig.getWidth());
        setHeight(enemyConfig.getHeight());
        Events.get().register(this);
    }

    public void setConfiguredLinearVelocity(Optional<Vector2> configuredLinearVelocity){
        this.configuredLinearVelocity = configuredLinearVelocity;
    }

    @Subscribe
    public void runnerHit(RunnerHitEvent runnerHitEvent){
        runnerActorOptional = Optional.of(runnerHitEvent.getRunnerActor());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final Body body = this.getBody();
        for(Vector2 velocity : configuredLinearVelocity.asSet()) {
            body.setLinearVelocity(velocity);
        }
        for (final ActorConfig actorConfig : this.getActorConfigOptional().asSet()) {
            if (!isWithinLeftBounds(actorConfig)) {
                Box2dFactory.destroyAndRemove(this);
            }
        }
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
        if(!hasRunnerArmour() && isRunnerHit()) {
            this.maintainPosition();
            this.drawFirstFrame(batch);
        } else {
            this.drawDefaultAnimation(batch);
        }
    }

    private boolean hasRunnerArmour() {
        for(RunnerActor runner : runnerActorOptional.asSet()){
            final AnimationState animationState = runner.getAnimationState();
            if(animationState.equals(AnimationState.ARMOUR_EQUIPPED)
                    || animationState.equals(AnimationState.ARMOUR_AND_GUN_EQUIPPED)){
                return true;
            }
        }
        return false;
    }

    private boolean isRunnerHit() {
        for(RunnerActor runner : runnerActorOptional.asSet()){
            if(runner.getActorState().equals(ActorState.HIT) ||
            runner.getActorState().equals(ActorState.FALLING)){
                return true;
            }
        }
        return false;
    }
}

package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.Enemy;
import com.nrg.kelly.physics.Box2dFactory;


public class EnemyBulletActor extends EnemyActor implements RayCastCallback {

    public EnemyBulletActor(Enemy enemy, CameraConfig cameraConfig) {
        super(enemy, cameraConfig);
    }

    @Override
    public void act(float delta) {

        final ActorState actorState = this.getActorState();
        if(!actorState.equals(ActorState.HIT_BY_ARMOUR)){
            this.checkRayCast();
        }
        super.act(delta);

    }
    public void checkRayCast(){

        final Vector2 position = this.getBody().getPosition();
        final Vector2 rayCastVector = new Vector2(position.x - 0.5f, position.y);
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
}

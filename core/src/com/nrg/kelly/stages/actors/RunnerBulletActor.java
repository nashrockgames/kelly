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
import com.nrg.kelly.config.actors.RunnerBulletConfig;
import com.nrg.kelly.events.BossHitEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.physics.Box2dFactory;


public class RunnerBulletActor extends EnemyActor implements RayCastCallback {

    public RunnerBulletActor(RunnerBulletConfig runnerBulletConfig, CameraConfig cameraConfig) {
        super(runnerBulletConfig, cameraConfig);
    }

    @Override
    public void act(float delta) {

        super.act(delta);
        this.checkRayCast();

    }
    public void checkRayCast(){

        final Vector2 position = this.getBody().getPosition();
        final Vector2 rayCastVector = new Vector2(position.x + 1.0f, position.y);
        Box2dFactory.getWorld().rayCast(this , position, rayCastVector);

    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

        final Object userData = fixture.getBody().getUserData();
        if(userData instanceof BossActor){
            Events.get().post(new BossHitEvent());
            final Body body = this.getBody();
            Box2dFactory.destroyBody(body);
            this.remove();
            return 0;
        }
        return 1;
    }


}

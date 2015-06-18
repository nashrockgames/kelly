package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.Enemy;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.physics.Box2dFactory;

public class EnemyActor extends GameActor {

    private Vector2 linearVelocity;

    public boolean runnerHit = false;

    public EnemyActor(Enemy enemy, CameraConfig cameraConfig) {
        super(enemy, cameraConfig);
        final Body body = Box2dFactory.getInstance().createEnemy(enemy);
        body.setUserData(this);
        setBody(body);
        setWidth(enemy.getWidth());
        setHeight(enemy.getHeight());
        Events.get().register(this);
    }

    public void setLinearVelocity(Vector2 linearVelocity){
        this.linearVelocity = linearVelocity;
    }

    @Subscribe
    public void runnerHit(RunnerHitEvent runnerHitEvent){
        this.runnerHit = true;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.getBody().setLinearVelocity(linearVelocity);
        final Body body = this.getBody();
        for(ActorConfig actorConfig : this.getConfig().asSet()) {
            if (!(body.getPosition().x + actorConfig.getWidth() / 2 > 0)) {
                if (!runnerHit) {
                    Box2dFactory.destroyBody(body);
                    this.remove();
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        if(runnerHit) {
            this.maintainPosition();
            this.drawFirstFrame(batch);
        } else {
            this.drawDefaultAnimation(batch);
        }
    }
}

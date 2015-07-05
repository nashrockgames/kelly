package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.Enemy;

import java.util.Set;


public class BossActor extends EnemyActor{

    private boolean isInFiringPosition = false;
    private Optional<Timer.Task> fireSchedule = Optional.absent();

    public BossActor(Enemy enemy, CameraConfig cameraConfig) {
        super(enemy, cameraConfig);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final Body body = this.getBody();
        for (ActorConfig actorConfig : this.getConfig().asSet()) {
            final float pos = this.getTextureBounds().getX() + this.getTextureBounds().getWidth();
            if(pos < Gdx.graphics.getWidth()){
                body.setLinearVelocity(0f, 0f);
                this.isInFiringPosition = true;
            } else {
                body.setLinearVelocity(linearVelocity);
            }
            if(canFireBullet()){
                fireBullet();
            }

        }
    }

    private boolean canFireBullet() {
        boolean canFire = isInFiringPosition;
        for(Timer.Task task : fireSchedule.asSet()) {
            if(task.isScheduled()){
                canFire = false;
            }
        }
        return canFire;
    }

    private void fireBullet() {

        fireSchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Gdx.app.log(this.getClass().getName(), "Fired bullet.");
            }
        }, 1.2f));

    }


}

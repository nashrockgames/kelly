package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.Enemy;
import com.nrg.kelly.events.BossFiredEvent;
import com.nrg.kelly.events.Events;

import java.util.Set;


public class BossActor extends EnemyActor{

    private boolean isInFiringPosition = false;
    private Optional<Timer.Task> fireSchedule = Optional.absent();
    public static final float FIRE_DELAY_SECONDS = 1.2f;

    private int bulletsFired = 0;

    public BossActor(Enemy enemy, CameraConfig cameraConfig) {
        super(enemy, cameraConfig);
    }

    public Optional<Timer.Task> getFireSchedule() {
        return fireSchedule;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final Body body = this.getBody();
        for (ActorConfig actorConfig : this.getConfig().asSet()) {
            final float pos = this.getTextureBounds().getX() + this.getTextureBounds().getWidth();
            updatePosition(body, pos);
            if(canFireBullet()){
                fireBullet();
            }
        }
    }

    private void updatePosition(Body body, float pos) {
        if(pos < Gdx.graphics.getWidth()){
            body.setLinearVelocity(0f, 0f);
            this.isInFiringPosition = true;
        } else {
            body.setLinearVelocity(linearVelocity);
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
        final BossActor instance = this;
        fireSchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Events.get().post(new BossFiredEvent(instance));
            }
        }, FIRE_DELAY_SECONDS));
        this.bulletsFired+=1;
    }

    public int getBulletsFired() {
        return bulletsFired;
    }
}

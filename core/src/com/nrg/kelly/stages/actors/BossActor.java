package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.Enemy;

public class BossActor extends EnemyActor{

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

            } else {
                body.setLinearVelocity(linearVelocity);
            }
        }
    }

}

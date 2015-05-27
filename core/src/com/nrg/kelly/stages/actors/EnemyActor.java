package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nrg.kelly.config.levels.Enemy;

/**
 * Created by Andrew on 27/05/2015.
 */
public class EnemyActor extends GameActor {

    private Vector2 linearVelocity;

    public EnemyActor(Body body) {
        this.setBody(body);
    }
    public void setLinearVelocity(Vector2 linearVelocity){
        this.linearVelocity = linearVelocity;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        this.getBody().setLinearVelocity(linearVelocity);
    }
}

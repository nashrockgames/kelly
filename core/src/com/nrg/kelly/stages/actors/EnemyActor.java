package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.levels.Enemy;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.OnEnemyDestroyedEvent;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.physics.Box2dFactory;

/**
 * Created by Andrew on 27/05/2015.
 */
public class EnemyActor extends GameActor {

    private final Enemy enemyConfig;
    private Vector2 linearVelocity;

    public boolean runnerHit = false;

    public EnemyActor(Enemy enemy) {
        this.enemyConfig = enemy;
        this.setBody(Box2dFactory.getInstance().createEnemy(enemy));
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
        if(!(body.getPosition().x + enemyConfig.getWidth() / 2 > 0)){
            if(!runnerHit){
                Box2dFactory.destroyBody(body);
                this.remove();
            }
        }
/*
        if (!BodyUtils.bodyInBounds(body)) {
            if (BodyUtils.bodyIsEnemy(body) && !runner.isHit()) {
                enemy++;
                if(enemyFactory.hasNextEnemy(level, enemy)) {
                    this.addActor(enemyFactory.createEnemy(level, enemy));
                }
            }
            world.destroyBody(body);
        }
*/

    }
}

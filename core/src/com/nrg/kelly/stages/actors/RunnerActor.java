package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.LeftSideScreenTouchDownEvent;
import com.nrg.kelly.events.screen.RightSideScreenTouchDownEvent;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;

import javax.inject.Inject;

/**
 * Created by Andrew on 2/05/2015.
 */
public class RunnerActor extends GameActor {

    final Box2dFactory box2dFactory = Box2dFactory.getInstance();

    private boolean hit = false;
    private boolean jumping = false;
    private boolean sliding = false;

    @Inject
    public RunnerActor() {
        setBody(box2dFactory.createRunner());
        Events.get().register(this);
    }

    @Subscribe
    public void beginContact(BeginContactEvent beginContactEvent){
        final Contact contact = beginContactEvent.getContact();
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final Object userData = bodyB.getUserData();
        if(bodyA.equals(this.getBody())){
            if(userData !=null) {
                if(userData instanceof GroundActor){
                    this.landed();
                }
                if (userData instanceof EnemyActor){
                    this.hit();
                }
            }
        }
    }

    @Subscribe
    public void jump(RightSideScreenTouchDownEvent rightSideScreenTouchDownEvent) {
        if (!(jumping || sliding || hit)) {
            final Body body = getBody();
            body.applyLinearImpulse(box2dFactory.getRunnerLinerImpulse(),
                    body.getWorldCenter(), true);
            jumping = true;
        }
    }

    @Subscribe
    public void slide(LeftSideScreenTouchDownEvent leftSideScreenTouchDownEvent){
        if( !(jumping || hit) ){
            final Body body = getBody();
            body.setTransform(box2dFactory.getSlidePosition(), box2dFactory.getSlideAngle());
            sliding = true;
        }
    }

    @Subscribe
    public void stopSliding(LeftSideScreenTouchUpEvent leftSideScreenTouchUpEvent){
        if(!(hit || jumping)) {
            final Body body = getBody();
            body.setTransform(box2dFactory.getRunPosition(), 0f);
            sliding = false;
        }
   }

    public void hit() {
        final Body body = getBody();
        body.applyAngularImpulse(box2dFactory.getHitAngularImpulse(), true);
        Events.get().post(new RunnerHitEvent());
        hit = true;
    }

    public void landed() {
       jumping = false;
    }


}
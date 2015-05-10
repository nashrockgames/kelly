package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.events.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.RightSideScreenTouchedEvent;
import com.nrg.kelly.physics.Box2dFactory;

import javax.inject.Inject;

/**
 * Created by Andrew on 2/05/2015.
 */
public class RunnerActor extends GameActor {

    @Inject
    Config config;

    @Inject
    GroundActor groundActor;

    final Box2dFactory box2dFactory = Box2dFactory.getInstance();

    private boolean jumping;

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
        if(bodyA==this.getBody() && bodyB.equals(groundActor.getBody())){
            this.landed();
        }


    }

    @Subscribe
    public void jump(RightSideScreenTouchedEvent rightSideScreenTouchedEvent) {

        if (!jumping) {
            final Body body = getBody();
            body.applyLinearImpulse(box2dFactory.getRunnerLinerImpulse(), body.getWorldCenter(), true);
            jumping = true;
        }

    }

    public void landed() {
       jumping = false;
    }


}
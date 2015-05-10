package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.physics.Box2dFactory;

import javax.inject.Inject;

/**
 * Created by Andrew on 2/05/2015.
 */
public class RunnerActor extends GameActor {

    @Inject
    Config config;

    final Box2dFactory box2dFactory = Box2dFactory.getInstance();

    private boolean jumping;

    @Inject
    public RunnerActor() {

        setBody(box2dFactory.createRunner());
    }

    public void jump() {

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
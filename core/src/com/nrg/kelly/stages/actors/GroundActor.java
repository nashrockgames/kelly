package com.nrg.kelly.stages.actors;

import com.nrg.kelly.config.Config;
import com.nrg.kelly.physics.Box2dFactory;

import javax.inject.Inject;

/**
 * Created by Andrew on 2/05/2015.
 */
public class GroundActor extends GameActor {

    @Inject
    Config config;

    @Inject
    public GroundActor() {
        setBody(Box2dFactory.getInstance().createGround());
    }

}
package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.physics.Worlds;

import javax.inject.Inject;

/**
 * Created by Andrew on 2/05/2015.
 */
public class RunnerActor extends GameActor {

    @Inject
    public RunnerActor() {
        setBody(Worlds.createRunner());
    }


}
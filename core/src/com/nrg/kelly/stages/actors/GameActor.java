package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Andrew on 2/05/2015.
 */
public abstract class GameActor extends Actor {

    private Body body;

    public void setBody(Body body) {
        this.body = body;
    }
}

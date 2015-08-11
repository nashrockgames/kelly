package com.nrg.kelly.stages;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.physics.BeginContactEvent;

import javax.inject.Inject;

/**
 * Created by Andrew on 10/08/2015.
 */
public class GameStageContactListener implements ContactListener{

    @Inject
    public GameStageContactListener(){

    }

    @Override
    public void beginContact(Contact contact) {
        Events.get().post(new BeginContactEvent(contact));
    }

    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}
}

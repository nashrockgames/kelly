package com.nrg.kelly.events;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by Andrew on 10/05/2015.
 */
public class BeginContactEvent {
    private final Contact contact;

    public BeginContactEvent(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}

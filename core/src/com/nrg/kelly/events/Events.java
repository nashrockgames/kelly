package com.nrg.kelly.events;

import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Andrew on 3/05/2015.
 */
@Singleton
public class Events {

    private static EventBus bus = new EventBus();
    @Inject
    public Events(){

    }

    public static EventBus get(){
        return bus;
    }

}

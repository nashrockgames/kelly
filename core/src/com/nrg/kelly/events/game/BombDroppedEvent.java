package com.nrg.kelly.events.game;

import com.nrg.kelly.stages.actors.BossActor;

/**
 * Created by Andrew on 21/07/2015.
 */
public class BombDroppedEvent {

    private final BossActor bossActor;

    public BombDroppedEvent(BossActor bossActor) {
        this.bossActor = bossActor;
    }

    public BossActor getBossActor() {
        return bossActor;
    }
}

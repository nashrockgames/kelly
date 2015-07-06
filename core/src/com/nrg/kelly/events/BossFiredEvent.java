package com.nrg.kelly.events;

import com.nrg.kelly.stages.actors.BossActor;

/**
 * Created by Andrew on 6/07/2015.
 */
public class BossFiredEvent {

    private final BossActor bossActor;

    public BossFiredEvent(BossActor bossActor) {
        this.bossActor = bossActor;
    }

    public BossActor getBossActor() {
        return bossActor;
    }
}

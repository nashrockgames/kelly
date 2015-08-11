package com.nrg.kelly.events.game;

import com.nrg.kelly.stages.actors.BossActor;

/**
 * Created by Andrew on 12/08/2015.
 */
public class SpawnBossBulletEvent {
    private final BossActor bossActor;

    public SpawnBossBulletEvent(BossActor bossActor) {
        this.bossActor = bossActor;
    }

    public BossActor getBossActor() {
        return bossActor;
    }
}

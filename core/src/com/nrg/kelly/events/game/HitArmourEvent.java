package com.nrg.kelly.events.game;

import com.nrg.kelly.stages.actors.EnemyActor;

public class HitArmourEvent {
    private final EnemyActor pauseTime;

    public HitArmourEvent(EnemyActor pauseTime) {
        this.pauseTime = pauseTime;
    }

    public EnemyActor getEnemyActor() {
        return pauseTime;
    }
}

package com.nrg.kelly.events.game;

import com.nrg.kelly.stages.actors.EnemyBombActor;

/**
 * Created by Andrew on 1/09/2015.
 */
public class BombExplodedEvent {
    private final EnemyBombActor enemyBombActor;

    public BombExplodedEvent(EnemyBombActor enemyBombActor) {
        this.enemyBombActor = enemyBombActor;
    }

    public EnemyBombActor getEnemyBombActor() {
        return enemyBombActor;
    }
}

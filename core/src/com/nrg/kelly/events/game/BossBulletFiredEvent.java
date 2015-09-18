package com.nrg.kelly.events.game;

import com.google.common.base.Optional;
import com.nrg.kelly.stages.actors.BossActor;
import com.nrg.kelly.stages.actors.RunnerActor;

/**
 * Created by Andrew on 6/07/2015.
 */
public class BossBulletFiredEvent {

    private final BossActor bossActor;
    private final Optional<RunnerActor> runnerActor;

    public BossBulletFiredEvent(BossActor bossActor, Optional<RunnerActor> runnerActor) {
        this.bossActor = bossActor;
        this.runnerActor = runnerActor;
    }

    public BossActor getBossActor() {
        return bossActor;
    }

    public Optional<RunnerActor> getRunnerActor() {
        return runnerActor;
    }
}

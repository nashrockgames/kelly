package com.nrg.kelly.events;

import com.google.common.base.Optional;
import com.nrg.kelly.stages.actors.RunnerActor;

/**
 * Created by Andrew on 10/08/2015.
 */
public class SpawnEnemyEvent {
    private final Optional<RunnerActor> runner;

    public SpawnEnemyEvent(Optional<RunnerActor> runner) {
        this.runner = runner;
    }

    public Optional<RunnerActor> getRunner() {
        return runner;
    }
}

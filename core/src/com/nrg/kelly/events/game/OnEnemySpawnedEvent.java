package com.nrg.kelly.events.game;

import com.google.common.base.Optional;
import com.nrg.kelly.stages.actors.RunnerActor;

public class OnEnemySpawnedEvent {

    private final Optional<RunnerActor> runner;

    public OnEnemySpawnedEvent(Optional<RunnerActor> runner) {
        this.runner = runner;
    }

    public Optional<RunnerActor> getRunner() {
        return runner;
    }


}

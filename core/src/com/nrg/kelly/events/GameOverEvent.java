package com.nrg.kelly.events;

import com.nrg.kelly.stages.actors.RunnerActor;

public class GameOverEvent {
    private final RunnerActor runnerActor;

    public GameOverEvent(RunnerActor runnerActor) {
        this.runnerActor = runnerActor;
    }

    public RunnerActor getRunnerActor() {
        return runnerActor;
    }
}


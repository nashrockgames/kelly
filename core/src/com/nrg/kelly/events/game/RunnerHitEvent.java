package com.nrg.kelly.events.game;

import com.nrg.kelly.stages.actors.RunnerActor;

public class RunnerHitEvent {

    private RunnerActor runnerActor;

    public RunnerHitEvent(RunnerActor runnerActor){
        this.runnerActor = runnerActor;
    }

    public RunnerActor getRunnerActor() {
        return runnerActor;
    }
}

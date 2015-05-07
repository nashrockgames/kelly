package com.nrg.kelly.stages;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrew on 7/05/2015.
 */
public class GameStageModel {

    @Inject
    RunnerActor runner;

    @Inject
    GroundActor ground;

    private List<Actor> actors = new ArrayList<Actor>();

    @Inject
    public GameStageModel(){

        actors.add(runner);
        actors.add(ground);

    }

    public List<Actor> getActors() {
        return actors;
    }

    public RunnerActor getRunner() {
        return runner;
    }

    public GroundActor getGround() {
        return ground;
    }


}

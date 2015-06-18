package com.nrg.kelly.inject;


import com.nrg.kelly.stages.Box2dGameModel;
import com.nrg.kelly.stages.actors.PlayButtonActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    Box2dGameModel provideGameStageModel(RunnerActor runnerActor,
                                         PlayButtonActor playButtonActor){
        Box2dGameModel gameStageModel = new Box2dGameModel();
        gameStageModel.getActors().add(runnerActor);
        gameStageModel.getActors().add(playButtonActor);
        return gameStageModel;
    }


}

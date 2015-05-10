package com.nrg.kelly.inject;

import com.nrg.kelly.stages.Box2dGameModel;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;
import com.nrg.kelly.stages.text.DebugText;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 10/05/2015.
 */
@Module
public class GameModelModule {

    @Provides
    Box2dGameModel provideGameStageModel(RunnerActor runnerActor,
                                         GroundActor groundActor){
        Box2dGameModel gameStageModel = new Box2dGameModel();
        gameStageModel.getActors().add(runnerActor);
        gameStageModel.getActors().add(groundActor);
        gameStageModel.getActors().add(new DebugText());
        return gameStageModel;
    }


}

package com.nrg.kelly.inject;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.stages.actors.ArmourActor;
import com.nrg.kelly.stages.actors.RunnerActor;

/**
 * Created by Andrew on 10/06/2015.
 */

public interface ActorFactory {

    Actor createEnemy(int level);
    Actor createBackground(int level);
    Actor createGround(int level);
    RunnerActor createRunner();
    ArmourActor createArmour();
    Actor createBoss(int level);
    void reset();


}

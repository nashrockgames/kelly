package com.nrg.kelly.inject;

import com.nrg.kelly.Main;
import com.nrg.kelly.physics.Worlds;
import com.nrg.kelly.screens.GameScreen;
import com.nrg.kelly.stages.GameStage;
import dagger.Module;

/**
 * Created by Andrew on 26/04/2015.
 */

@Module(
        injects = {
            Main.class,
            GameStage.class,
            GameScreen.class,
            Worlds.class
        }
    )
public class GameModule {

}

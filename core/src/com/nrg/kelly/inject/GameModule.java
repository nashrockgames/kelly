package com.nrg.kelly.inject;

import com.badlogic.gdx.physics.box2d.World;
import com.nrg.kelly.physics.Worlds;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {
    @Provides
    World provideWorld(){
        return Worlds.getWorld();
    }

}

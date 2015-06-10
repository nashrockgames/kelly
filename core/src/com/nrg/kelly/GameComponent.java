package com.nrg.kelly;
import com.nrg.kelly.inject.GameModule;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = GameModule.class)
public interface GameComponent {

    KellyGame getKellyGame();

}

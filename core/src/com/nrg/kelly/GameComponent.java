package com.nrg.kelly;
import com.nrg.kelly.inject.GameModule;

import dagger.Component;

@Component(modules = GameModule.class)
public interface GameComponent {

    KellyGame getKellyGame();

}

package com.nrg.kelly;


public interface GameStateManager {

    GameState getGameState();

    BossState getBossState();

    void setGameState(GameState gameState);

    void setBossState(BossState bossState);

}

package com.nrg.kelly;


import com.nrg.kelly.stages.actors.RunnerActor;

public interface GameStateManager {

    GameState getGameState();

    BossState getBossState();

    void setGameState(GameState gameState);

    void setBossState(BossState bossState);

    public boolean canSpawnEnemy(RunnerActor runnerActor);

    public boolean canSpawnBoss();

    public boolean canSpawnArmour(RunnerActor runnerActor);

    public boolean canSpawnGun(RunnerActor runnerActor);

}

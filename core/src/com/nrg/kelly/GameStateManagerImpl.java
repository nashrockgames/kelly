package com.nrg.kelly;

import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.stages.actors.ActorState;
import com.nrg.kelly.stages.actors.AnimationState;
import com.nrg.kelly.stages.actors.RunnerActor;

import javax.inject.Inject;

/**
 * Created by Andrew on 19/06/2015.
 */
public class GameStateManagerImpl implements GameStateManager{

    private GameState gameState;

    private BossState bossState;

    @Inject
    public GameStateManagerImpl(){
        Events.get().register(this);
    }

    @Subscribe
    public void onBuildGame(PostBuildGameModuleEvent postBuildGameModuleEvent){
        gameState = GameState.PAUSED;
    }

    public boolean canSpawnEnemy(RunnerActor runnerActor){
        final ActorState actorState = runnerActor.getActorState();
        final BossState bossState = getBossState();
        return !bossState.equals(BossState.SPAWNING) &&
                !actorState.equals(ActorState.HIT) &&
                !actorState.equals(ActorState.FALLING);
    }

    @Override
    public boolean canSpawnBoss() {
        final BossState bossState = getBossState();
        final GameState gameState = getGameState();
        return bossState.equals(BossState.NONE) &&
                gameState.equals(GameState.PLAYING);
    }

    @Override
    public boolean canSpawnGun(RunnerActor runnerActor) {

        final ActorState actorState = runnerActor.getActorState();
        final BossState bossState = getBossState();
        final AnimationState animationState = runnerActor.getAnimationState();

        return !bossState.equals(BossState.SPAWNING) &&
                !actorState.equals(ActorState.HIT) &&
                !animationState.equals(AnimationState.GUN_EQUIPPED) &&
                !animationState.equals(AnimationState.ARMOUR_AND_GUN_EQUIPPED) &&
                !actorState.equals(ActorState.FALLING);

    }


    @Override
    public boolean canSpawnArmour(RunnerActor runnerActor){
        final ActorState actorState = runnerActor.getActorState();
        final BossState bossState = getBossState();
        final AnimationState animationState = runnerActor.getAnimationState();

        return !bossState.equals(BossState.SPAWNING) &&
                !actorState.equals(ActorState.HIT) &&
                !animationState.equals(AnimationState.ARMOUR_EQUIPPED) &&
                !animationState.equals(AnimationState.ARMOUR_AND_GUN_EQUIPPED) &&
                !actorState.equals(ActorState.FALLING);
    }


    public GameState getGameState() {
        return gameState;
    }

    @Override
    public BossState getBossState() {
        return bossState;
    }

    @Override
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void setBossState(BossState bossState) {
        this.bossState = bossState;
    }


}


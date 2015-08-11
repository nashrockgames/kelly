package com.nrg.kelly.stages;

import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.BossState;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.events.BulletFiredEvent;
import com.nrg.kelly.events.EnemySpawnTimeReducedEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.SpawnEnemyEvent;
import com.nrg.kelly.events.SpawnGunEvent;
import com.nrg.kelly.events.game.CancelSchedulesEvent;
import com.nrg.kelly.events.game.OnEnemySpawnedEvent;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.game.SpawnArmourEvent;
import com.nrg.kelly.events.game.SpawnBossBulletEvent;
import com.nrg.kelly.events.game.SpawnBossEvent;
import com.nrg.kelly.stages.actors.BossActor;
import com.nrg.kelly.stages.actors.RunnerActor;

import javax.inject.Inject;

/**
 * Created by Andrew on 10/08/2015.
 */
public class GameStageController {

    @Inject
    GameStateManager gameStateManager;

    private Optional<Timer.Task> enemySchedule = Optional.absent();

    private float enemySpawnDelaySeconds;
    private float reduceEnemySpawnIntervalSeconds;
    private float reduceEnemySpawnDelayPercentage;
    private int spawnBossOnEnemyCount;
    private Optional<Timer.Task> enemySpawnTimeReduceTask = Optional.absent();
    private int enemiesSpawned = 0;

    @Inject
    public GameStageController(){
        Events.get().register(this);
    }

    @Subscribe
    public void cancelSchedules(CancelSchedulesEvent cancelSchedulesEvent) {
        if (this.enemySchedule.isPresent()) {
            this.enemySchedule.get().cancel();
        }
        if(this.enemySpawnTimeReduceTask.isPresent()){
            enemySpawnTimeReduceTask.get().cancel();
        }
    }

    @Subscribe
    public void onBossFiredEvent(BulletFiredEvent bulletFiredEvent){
        if(gameStateManager.getGameState().equals(GameState.PLAYING)) {
            final BossActor bossActor = bulletFiredEvent.getBossActor();
            Events.get().post(new SpawnBossBulletEvent(bossActor));
            for (RunnerActor runnerActor : bulletFiredEvent.getRunnerActor().asSet()) {
                final int bulletsFired = bossActor.getBulletsFired();
                if (bulletsFired > 0) {
                    if (bulletsFired % bossActor.getArmourSpawnInterval() == 0) {
                        if (gameStateManager.canSpawnArmour(runnerActor)) {
                            Events.get().post(new SpawnArmourEvent());
                        }
                    }
                    if (bulletsFired % bossActor.getGunSpawnInterval() == 0) {
                        if (gameStateManager.canSpawnGun(runnerActor)) {
                            Events.get().post(new SpawnGunEvent());
                        }
                    }
                }
            }
        }
    }



    @Subscribe
    public void onEnemySpawned(OnEnemySpawnedEvent onEnemySpawnedEvent){

        if(enemiesSpawned == spawnBossOnEnemyCount){
            if(gameStateManager.canSpawnBoss()) {
                Events.get().post(new SpawnBossEvent());
                this.gameStateManager.setBossState(BossState.SPAWNING);
            }
        } else {
            final BossState bossState = this.gameStateManager.getBossState();
            if(bossState.equals(BossState.NONE)) {
                this.scheduleSpawnEnemy(onEnemySpawnedEvent.getRunner());
            }
        }

    }

    public void scheduleSpawnEnemy(final Optional<RunnerActor> runner) {
        this.enemySchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (gameStateManager.getGameState().equals(GameState.PLAYING)) {
                    for (final RunnerActor runnerActor : runner.asSet()) {
                        if (gameStateManager.canSpawnEnemy(runnerActor)) {
                            Events.get().post(new SpawnEnemyEvent(runner));
                            enemiesSpawned++;
                        }
                    }
                }
            }
        }, enemySpawnDelaySeconds));
    }


    @Subscribe
    public void onReduceEnemySpawnDelay(EnemySpawnTimeReducedEvent enemySpawnTimeReducedEvent){

        enemySpawnTimeReduceTask = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                enemySpawnDelaySeconds = enemySpawnDelaySeconds -
                        (enemySpawnDelaySeconds * reduceEnemySpawnDelayPercentage);
                Events.get().post(new EnemySpawnTimeReducedEvent());
            }
        }, reduceEnemySpawnIntervalSeconds));
    }

    public void setEnemySpawnDelaySeconds(float enemySpawnDelaySeconds) {
        this.enemySpawnDelaySeconds = enemySpawnDelaySeconds;
    }

    public float getEnemySpawnDelaySeconds() {
        return enemySpawnDelaySeconds;
    }

    public void setEnemiesSpawned(int enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    public int getEnemiesSpawned() {
        return enemiesSpawned;
    }

    public void setReduceEnemySpawnIntervalSeconds(float reduceEnemySpawnIntervalSeconds) {
        this.reduceEnemySpawnIntervalSeconds = reduceEnemySpawnIntervalSeconds;
    }

    public float getReduceEnemySpawnIntervalSeconds() {
        return reduceEnemySpawnIntervalSeconds;
    }

    public void setReduceEnemySpawnDelayPercentage(float reduceEnemySpawnDelayPercentage) {
        this.reduceEnemySpawnDelayPercentage = reduceEnemySpawnDelayPercentage;
    }

    public float getReduceEnemySpawnDelayPercentage() {
        return reduceEnemySpawnDelayPercentage;
    }

    public void setSpawnBossOnEnemyCount(int spawnBossOnEnemyCount) {
        this.spawnBossOnEnemyCount = spawnBossOnEnemyCount;
    }

    public int getSpawnBossOnEnemyCount() {
        return spawnBossOnEnemyCount;
    }
}

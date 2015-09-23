package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.EnemyBossConfig;
import com.nrg.kelly.config.actors.PositionConfig;
import com.nrg.kelly.events.BossHitEvent;
import com.nrg.kelly.events.game.BombDroppedEvent;
import com.nrg.kelly.events.game.BossDeadEvent;
import com.nrg.kelly.events.game.BossDeathEvent;
import com.nrg.kelly.events.game.BossBulletFiredEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.CancelSchedulesEvent;
import com.nrg.kelly.events.game.GameEvent;
import com.nrg.kelly.events.game.GameOverEvent;
import com.nrg.kelly.events.game.RunnerEndLevelEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;

import java.util.List;

public class BossActor extends EnemyActor {

    public static final float DYING_TIME = 1.5f;
    public static final float HIT_TIME = 0.5f;
    private final EnemyBossConfig bossConfig;
    private boolean isInFiringPosition = false;
    private Optional<Timer.Task> fireBulletSchedule = Optional.absent();
    private Optional<Timer.Task> fireIntervalSchedule = Optional.absent();
    private Optional<PositionConfig> endOfLevelPosition = Optional.absent();


    //TODO: make configurable
    public static final float FIRE_BULLET_DELAY_SECONDS = 1.2f;
    public static final int FIRE_BULLET_INTERVAL_COUNT = 3;
    public static final float FIRE_BULLETS_INTERVAL = 3.5f;
    public static final float DROP_BOMB_DELAY_SECONDS = 2.0f;
    private final int maxHitCount = 10;

    private int bulletsFired = 0;
    private int armourSpawnInterval;
    private int gunSpawnInterval;
    private Optional<RunnerActor> runnerActor;
    private final Animation hitAnimation;
    private final AtlasConfig hitAtlasConfig;
    private final AtlasConfig dyingAtlasConfig;
    private final AtlasConfig deadAtlasConfig;
    private final AtlasConfig endAtlasConfig;
    private final Animation endAnimation;
    private final Animation dyingAnimation;
    private final Animation deadAnimation;
    private boolean paused;
    private int hitCount = 0;
    private Optional<Timer.Task> bombSchedule = Optional.absent();
    private Optional<Timer.Task> hitSchedule = Optional.absent();
    private Optional<Timer.Task> dyingSchedule = Optional.absent();


    public BossActor(EnemyBossConfig enemyConfig, CameraConfig cameraConfig) {
        super(enemyConfig, cameraConfig);
        this.bossConfig = enemyConfig;

        final List<AtlasConfig> animations = enemyConfig.getAnimations();
        hitAtlasConfig = this.getAtlasConfigByName(animations, "cart_boss_hit");
        final TextureAtlas hitAtlas = new TextureAtlas(Gdx.files.internal(hitAtlasConfig.getAtlas()));
        hitAnimation = new Animation(enemyConfig.getFrameRate(), hitAtlas.getRegions());

        dyingAtlasConfig = this.getAtlasConfigByName(animations, "cart_boss_dying");
        final TextureAtlas dyingAtlas = new TextureAtlas(Gdx.files.internal(dyingAtlasConfig.getAtlas()));
        dyingAnimation = new Animation(enemyConfig.getFrameRate(), dyingAtlas.getRegions());

        deadAtlasConfig = this.getAtlasConfigByName(animations, "cart_boss_dead");
        final TextureAtlas deadAtlas = new TextureAtlas(Gdx.files.internal(deadAtlasConfig.getAtlas()));
        deadAnimation = new Animation(enemyConfig.getFrameRate(), deadAtlas.getRegions());

        endAtlasConfig = this.getAtlasConfigByName(animations, "cart_boss_end");
        final TextureAtlas endAtlas = new TextureAtlas(Gdx.files.internal(endAtlasConfig.getAtlas()));
        endAnimation = new Animation(enemyConfig.getFrameRate(), endAtlas.getRegions());


        this.setArmourSpawnInterval(enemyConfig.getArmourSpawnInterval());
        this.setGunSpawnInterval(enemyConfig.getGunSpawnInterval());
    }

    @Subscribe
    public void onBossDead(BossDeadEvent bossDeadEvent){

        final PositionConfig endOfLevelPosition = this.bossConfig.getEndOfLevelPosition();
        final float endVelocityX = this.bossConfig.getEndOfLevelVelocityX();
        final float endVelocityY = this.bossConfig.getEndOfLevelVelocityY();
        moveToPosition(endOfLevelPosition, endVelocityX, endVelocityY);

    }

    private void moveToPosition(PositionConfig position, float endVelocityX, float endVelocityY) {
        this.clearForcedTransform();
        this.unMaintainPosition();
        this.setEndOfLevelPosition(Optional.fromNullable(position));
        this.setForcedLinearVelocity(endVelocityX, endVelocityY);
    }

    public void setEndOfLevelPosition(Optional<PositionConfig> endOfLevelPosition) {
        this.endOfLevelPosition = endOfLevelPosition;
    }

    @Subscribe
    public void onRunnerEndLevel(RunnerEndLevelEvent runnerEndLevelEvent){
        this.setActorState(ActorState.END_LEVEL);
    }

    public Optional<Timer.Task> getFireBulletSchedule() {
        return fireBulletSchedule;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(endOfLevelPosition.isPresent()){
            if(this.getBody().getPosition().x <= endOfLevelPosition.get().getX()){
                clearForcedLinearVelocity();
                maintainPosition();
            }
        }
        maybeFireWeapon();

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        //super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final TextureRegion region;
        final ActorState state = this.getActorState();
        switch(state){
            case HIT:
                region = this.hitAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(hitAtlasConfig.getImageOffset()),
                        Optional.of(hitAtlasConfig.getImageScale()));
                break;
            case DYING:
                region = this.dyingAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(dyingAtlasConfig.getImageOffset()),
                        Optional.of(dyingAtlasConfig.getImageScale()));
                break;
            case DEAD:
                region = this.deadAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(dyingAtlasConfig.getImageOffset()),
                        Optional.of(dyingAtlasConfig.getImageScale()));
                break;
            case END_LEVEL:
                region = this.endAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(endAtlasConfig.getImageOffset()),
                        Optional.of(endAtlasConfig.getImageScale()));
                break;
            case RUNNING:
                drawDefaultAnimation(batch);
                break;
            default:
                drawDefaultAnimation(batch);
                break;
        }
    }

    private void maybeFireWeapon() {

        final Body body = this.getBody();
        for (final ActorConfig actorConfig : this.getConfig().asSet()) {
            if(!this.getForcedLinearVelocity().isPresent()) {
                holdPositionWhenInView(body);
            }
            if(this.isInFiringPosition) {
                if (canFireWeapon()) {
                    fireWeapon();
                }
            }
        }
    }

    @Subscribe
    public void onHit(BossHitEvent bossHitEvent){
        final ActorState actorState = this.getActorState();
        if(actorState.equals(ActorState.DYING) || actorState.equals(ActorState.DEAD))
            return;
        if(hitCount == this.maxHitCount){
            if(!dyingSchedule.isPresent() && !actorState.equals(ActorState.DYING)){
                this.setActorState(ActorState.DYING);
                this.cancelSchedules(null);
                final Optional<BossDeathEvent> bossDeathEvent = Optional.of(new BossDeathEvent());
                dyingSchedule = scheduleState(ActorState.DEAD, DYING_TIME, bossDeathEvent);
            }
        } else if(actorState.equals(ActorState.RUNNING)) {
            this.setActorState(ActorState.HIT);
            Optional<BossDeathEvent> absentEvent = Optional.absent();
            hitSchedule = scheduleState(ActorState.RUNNING, HIT_TIME, absentEvent);
        }
        this.hitCount++;

    }

    private Optional<Timer.Task> scheduleState(final ActorState actorState,
                                               final float time,
                                               final Optional<? extends GameEvent> optionalEvent) {
        return Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                setActorState(actorState);
                for(GameEvent event : optionalEvent.asSet()){
                    Events.get().post(event);
                }
            }
        }, time));
    }

    @Subscribe
    public void onGameOver(GameOverEvent gameOverEvent){
        paused = true;
    }

    @Subscribe
    public void onGameStart(PlayButtonClickedEvent playButtonClickedEvent){
        paused = false;
    }

    private void holdPositionWhenInView(Body body) {


        final CameraConfig cameraConfig = this.getCameraConfig();
        float finalBodyPosition = cameraConfig.getViewportWidth() - (this.getWidth() / 2f);
        float currentPosition = body.getPosition().x;
        if (currentPosition < finalBodyPosition) {
            this.maintainPosition();
            body.setLinearVelocity(0f, 0f);
            this.isInFiringPosition = true;
        } else {
            for(final Vector2 vector : configuredLinearVelocity.asSet()) {
                body.setLinearVelocity(vector);
            }
        }
    }

    private boolean canFireWeapon() {
        //TODO: this is ugly
        final ActorState actorState = this.getActorState();
        if(actorState.equals(ActorState.DYING))
            return false;

        boolean canFire = actorState.equals(ActorState.RUNNING) ||
                actorState.equals(ActorState.HIT);

        for (final Timer.Task intervalTask : fireIntervalSchedule.asSet()) {
            canFire = !intervalTask.isScheduled();
        }
        if(canFire) {
            for (final Timer.Task fireBulletTask : fireBulletSchedule.asSet()) {
                canFire = !fireBulletTask.isScheduled();
            }
        }

        return canFire;
    }

    private void fireWeapon() {

        final BossActor instance = this;
        fireBulletSchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Events.get().post(new BossBulletFiredEvent(instance, runnerActor));
            }
        }, FIRE_BULLET_DELAY_SECONDS));

        this.bulletsFired += 1;

        if (this.bulletsFired % FIRE_BULLET_INTERVAL_COUNT == 0) {
            this.dropBomb();
            fireIntervalSchedule = Optional.of(Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    //this is just a pause interval. Does nothing.
                }
            }, FIRE_BULLETS_INTERVAL));
        }

    }

    private void dropBomb() {

        bombSchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Events.get().post(new BombDroppedEvent());
            }
        }, DROP_BOMB_DELAY_SECONDS));
    }

    @Subscribe
    public void cancelSchedules(CancelSchedulesEvent cancelSchedulesEvent) {
        cancelSchedule(this.fireBulletSchedule);
        cancelSchedule(this.fireIntervalSchedule);
        cancelSchedule(this.bombSchedule);
        cancelSchedule(this.hitSchedule);
        cancelSchedule(this.dyingSchedule);

    }

    private void cancelSchedule(Optional<Timer.Task> taskOptional){
        for(Timer.Task task : taskOptional.asSet()){
            if( task.isScheduled() ){
                task.cancel();
            }
        }
    }

    public int getBulletsFired() {
        return bulletsFired;
    }

    public void setArmourSpawnInterval(int armourSpawnInterval) {
        this.armourSpawnInterval = armourSpawnInterval;
    }

    public int getArmourSpawnInterval() {
        return armourSpawnInterval;
    }


    public void setGunSpawnInterval(int gunSpawnInterval) {
        this.gunSpawnInterval = gunSpawnInterval;
    }

    public int getGunSpawnInterval() {
        return gunSpawnInterval;
    }

    public void setRunnerActor(Optional<RunnerActor> runnerActor) {
        this.runnerActor = runnerActor;
    }

    public Optional<RunnerActor> getRunnerActor() {
        return runnerActor;
    }

}

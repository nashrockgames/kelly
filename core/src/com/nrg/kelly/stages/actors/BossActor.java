package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.EnemyBossConfig;
import com.nrg.kelly.events.BossHitEvent;
import com.nrg.kelly.events.game.BombDroppedEvent;
import com.nrg.kelly.events.game.BulletFiredEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.CancelSchedulesEvent;
import com.nrg.kelly.events.game.GameOverEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;

import java.util.List;

public class BossActor extends EnemyActor {

    private boolean isInFiringPosition = false;
    private Optional<Timer.Task> fireBulletSchedule = Optional.absent();
    private Optional<Timer.Task> fireIntervalSchedule = Optional.absent();

    //TODO: make configurable
    public static final float FIRE_BULLET_DELAY_SECONDS = 1.2f;
    public static final int FIRE_BULLET_INTERVAL_COUNT = 3;
    public static final float FIRE_BULLETS_INTERVAL = 3.5f;
    public static final float DROP_BOMB_DELAY_SECONDS = 2.0f;

    private int bulletsFired = 0;
    private int armourSpawnInterval;
    private int gunSpawnInterval;
    private Optional<RunnerActor> runnerActor;
    private final Animation hitAnimation;
    private final AtlasConfig hitAtlasConfig;
    private boolean paused;


    public BossActor(EnemyBossConfig enemyConfig, CameraConfig cameraConfig) {
        super(enemyConfig, cameraConfig);

        final List<AtlasConfig> animations = enemyConfig.getAnimations();
        hitAtlasConfig = this.getAtlasConfigByName(animations, "cart_boss_hit");
        final TextureAtlas hitAtlas = new TextureAtlas(Gdx.files.internal(hitAtlasConfig.getAtlas()));
        hitAnimation = new Animation(enemyConfig.getFrameRate(), hitAtlas.getRegions());
        this.setArmourSpawnInterval(enemyConfig.getArmourSpawnInterval());
        this.setGunSpawnInterval(enemyConfig.getGunSpawnInterval());
    }

    public Optional<Timer.Task> getFireBulletSchedule() {
        return fireBulletSchedule;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
            holdPositionWhenInView(body);
            if (canFireWeapon()) {
                fireWeapon();
            }
        }
    }

    @Subscribe
    public void onHit(BossHitEvent bossHitEvent){
        this.setActorState(ActorState.HIT);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                setActorState(ActorState.RUNNING);
            }
        }, 1.5f);
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
            body.setLinearVelocity(configuredLinearVelocity);
        }
    }

    private boolean canFireWeapon() {
        if(paused)
            return false;

        boolean canFire = isInFiringPosition;
        for (final Timer.Task intervalTask : fireIntervalSchedule.asSet()) {
            if (intervalTask.isScheduled()) {
                canFire = false;
            }
        }
        for (final Timer.Task task : fireBulletSchedule.asSet()) {
            if (task.isScheduled()) {
                canFire = false;
            }
        }
        return canFire;
    }

    private void fireWeapon() {

        final BossActor instance = this;
        fireBulletSchedule = Optional.of(Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                Events.get().post(new BulletFiredEvent(instance, runnerActor));
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
        final BossActor instance = this;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(instance.getActorState().equals(ActorState.RUNNING)) {
                    Events.get().post(new BombDroppedEvent(instance));
                }
            }
        }, DROP_BOMB_DELAY_SECONDS);
    }

    @Subscribe
    public void cancelSchedules(CancelSchedulesEvent cancelSchedulesEvent) {
        cancelSchedule(this.fireBulletSchedule);
        cancelSchedule(this.fireIntervalSchedule);
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

    @Override
    public boolean remove(){
        this.setActorState(ActorState.REMOVED);
        return super.remove();
    }

    public Optional<RunnerActor> getRunnerActor() {
        return runnerActor;
    }
}

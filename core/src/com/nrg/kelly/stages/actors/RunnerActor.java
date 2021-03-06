package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.HitVectorConfig;
import com.nrg.kelly.config.actors.ImageOffsetConfig;
import com.nrg.kelly.config.actors.ImageScaleConfig;
import com.nrg.kelly.config.actors.PositionConfig;
import com.nrg.kelly.events.game.ArmourPickedUpEvent;
import com.nrg.kelly.events.game.BossDeadEvent;
import com.nrg.kelly.events.game.FireRunnerWeaponEvent;
import com.nrg.kelly.events.game.GameOverEvent;
import com.nrg.kelly.config.actors.RunnerConfig;
import com.nrg.kelly.events.game.GunPickedUpEvent;
import com.nrg.kelly.events.game.RunnerEndLevelEvent;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.OnDoubleTapGestureEvent;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.events.game.JumpControlInvokedEvent;
import com.nrg.kelly.physics.Box2dFactory;

import java.util.List;

public class RunnerActor extends GameActor {

    private Animation jumpAnimation;
    private Animation slideAnimation;
    private Animation dieAnimation;
    private AtlasConfig jumpAtlasConfig;
    private AtlasConfig slideAtlasConfig;
    private AtlasConfig dieAtlasConfig;
    private boolean deathScheduled = false;
    private RunnerConfig runnerConfig;
    private AtlasConfig armourJumpAtlasConfig;
    private AtlasConfig armourSlideAtlasConfig;
    private AtlasConfig armourRunAtlasConfig;
    private Animation armourJumpAnimation;
    private Animation armourSlideAnimation;
    private Animation armourRunAnimation;
    private AnimationState animationState = AnimationState.DEFAULT;
    private AtlasConfig runGunAtlasConfig;
    private AtlasConfig jumpGunAtlasConfig;
    private AtlasConfig armourRunGunAtlasConfig;
    private AtlasConfig slideGunAtlasConfig;
    private AtlasConfig armourSlideGunAtlasConfig;
    private AtlasConfig armourJumpGunAtlasConfig;
    private Animation runGunAnimation;
    private Animation jumpGunAnimation;
    private Animation slideGunAnimation;
    private Animation armourJumpGunAnimation;
    private Animation armourSlideGunAnimation;
    private Animation armourRunGunAnimation;
    private Optional<PositionConfig> endOfLevelPosition = Optional.absent();
    private boolean destroyOnNextCollision = false;
    private boolean collidedWithDeadBoss = false;

    public RunnerActor(RunnerConfig runnerConfig, CameraConfig cameraConfig) {
        super(runnerConfig, cameraConfig);
        this.runnerConfig = runnerConfig;
        Events.get().register(this);
        setWidth(runnerConfig.getWidth());
        setHeight(runnerConfig.getHeight());
        this.createTextures();
    }

    public void createTextures(){
        //TODO: this is repetitive and should be abstracted
        final List<AtlasConfig> atlasConfigList = runnerConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));
        runGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "run-gun");

        jumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "jump");
        jumpGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "jump-gun");

        slideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "slide");
        slideGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "slide-gun");

        dieAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "die");

        armourJumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-jump");
        armourJumpGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-jump-gun");

        armourSlideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-slide");
        armourSlideGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-slide-gun");

        armourRunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-run");
        armourRunGunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-run-gun");

        final String run = getDefaultAtlasConfig().getAtlas();
        final String runGun = runGunAtlasConfig.getAtlas();

        final String jump = jumpAtlasConfig.getAtlas();
        final String jumpGun = jumpGunAtlasConfig.getAtlas();

        final String slide = slideAtlasConfig.getAtlas();
        final String slideGun = slideGunAtlasConfig.getAtlas();

        final String die = dieAtlasConfig.getAtlas();

        final String armourRun = armourRunAtlasConfig.getAtlas();
        final String armourRunGun = armourRunGunAtlasConfig.getAtlas();

        final String armourJump = armourJumpAtlasConfig.getAtlas();
        final String armourJumpGun = armourJumpGunAtlasConfig.getAtlas();

        final String armourSlide = armourSlideAtlasConfig.getAtlas();
        final String armourSlideGun = armourSlideGunAtlasConfig.getAtlas();

        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(run));
        final TextureAtlas runGunAtlas = new TextureAtlas(Gdx.files.internal(runGun));

        final TextureAtlas jumpAtlas = new TextureAtlas(Gdx.files.internal(jump));
        final TextureAtlas jumpGunAtlas = new TextureAtlas(Gdx.files.internal(jumpGun));

        final TextureAtlas slideAtlas = new TextureAtlas(Gdx.files.internal(slide));
        final TextureAtlas slideGunAtlas = new TextureAtlas(Gdx.files.internal(slideGun));

        final TextureAtlas dieAtlas = new TextureAtlas(Gdx.files.internal(die));

        final TextureAtlas armourJumpAtlas = new TextureAtlas(Gdx.files.internal(armourJump));
        final TextureAtlas armourJumpGunAtlas = new TextureAtlas(Gdx.files.internal(armourJumpGun));

        final TextureAtlas armourSlideAtlas = new TextureAtlas(Gdx.files.internal(armourSlide));
        final TextureAtlas armourSlideGunAtlas = new TextureAtlas(Gdx.files.internal(armourSlideGun));

        final TextureAtlas armourRunAtlas = new TextureAtlas(Gdx.files.internal(armourRun));
        final TextureAtlas armourRunGunAtlas = new TextureAtlas(Gdx.files.internal(armourRunGun));

        setDefaultAnimation(new Animation(runnerConfig.getFrameRate(), defaultAtlas.getRegions()));
        runGunAnimation = new Animation(runnerConfig.getFrameRate(), runGunAtlas.getRegions());

        jumpAnimation = new Animation(runnerConfig.getFrameRate(), jumpAtlas.getRegions());
        jumpGunAnimation = new Animation(runnerConfig.getFrameRate(), jumpGunAtlas.getRegions());

        slideAnimation = new Animation(runnerConfig.getFrameRate(), slideAtlas.getRegions());
        slideGunAnimation = new Animation(runnerConfig.getFrameRate(), slideGunAtlas.getRegions());

        dieAnimation = new Animation(runnerConfig.getFrameRate(), dieAtlas.getRegions());

        armourJumpAnimation = new Animation(runnerConfig.getFrameRate(), armourJumpAtlas.getRegions());
        armourJumpGunAnimation = new Animation(runnerConfig.getFrameRate(), armourJumpGunAtlas.getRegions());

        armourSlideAnimation = new Animation(runnerConfig.getFrameRate(), armourSlideAtlas.getRegions());
        armourSlideGunAnimation = new Animation(runnerConfig.getFrameRate(), armourSlideGunAtlas.getRegions());

        armourRunAnimation = new Animation(runnerConfig.getFrameRate(), armourRunAtlas.getRegions());
        armourRunGunAnimation = new Animation(runnerConfig.getFrameRate(), armourRunGunAtlas.getRegions());


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final TextureRegion region;
        final ActorState state = this.getActorState();
        final AnimationState animationState = this.getAnimationState();
        final Animation animation;
        final AtlasConfig atlasConfig;
        switch(state){
            case UPGRADING_ARMOUR:
                drawUpgradingAnimation(batch, animationState);
                break;
            case UPGRADING_GUN:
                drawUpgradingAnimation(batch, animationState);
                break;
            case SLIDING:
                animation = getSlideAnimation(animationState);
                region = animation.getKeyFrame(stateTime, true);
                drawSlideAnimation(batch, region);
                break;
            case JUMPING:
                animation = getJumpAnimation(animationState);
                region = animation.getKeyFrame(stateTime, true);
                atlasConfig = getJumpAtlasConfig(animationState);
                drawAnimation(batch,
                        region,
                        Optional.of(atlasConfig.getImageOffset()),
                        Optional.of(atlasConfig.getImageScale()));
                break;
            case HIT:
                maintainPosition();
                region = dieAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(dieAtlasConfig.getImageOffset()),
                        Optional.of(dieAtlasConfig.getImageScale()));

                break;
            case FALLING:
                region = dieAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(dieAtlasConfig.getImageOffset()),
                        Optional.of(dieAtlasConfig.getImageScale()));
                if(this.getBody().getPosition().y < 0){
                    Events.get().post(new GameOverEvent(this));
                }
                break;
            case RUNNING:
                animation = getRunAnimation(animationState);
                region = animation.getKeyFrame(stateTime, true);
                atlasConfig = getRunAtlasConfig(animationState);
                drawAnimation(batch,
                        region,
                        Optional.of(atlasConfig.getImageOffset()),
                        Optional.of(atlasConfig.getImageScale()));
                break;
        }

    }

    public boolean canFireWeapon() {
        return getAnimationState().equals(AnimationState.ARMOUR_AND_GUN_EQUIPPED)||
                getAnimationState().equals(AnimationState.GUN_EQUIPPED);
    }


    private AtlasConfig getRunAtlasConfig(final AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourRunAtlasConfig;
            case ARMOUR_AND_GUN_EQUIPPED:
                return armourRunGunAtlasConfig;
            case GUN_EQUIPPED:
                return runGunAtlasConfig;
            default:
                return getDefaultAtlasConfig();
        }
    }

    private Animation getRunAnimation(final AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourRunAnimation;
            case ARMOUR_AND_GUN_EQUIPPED:
                return armourRunGunAnimation;
            case GUN_EQUIPPED:
                return runGunAnimation;
            default:
                return getDefaultAnimation();
        }
    }

    private AtlasConfig getJumpAtlasConfig(final AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourJumpAtlasConfig;
            case ARMOUR_AND_GUN_EQUIPPED:
                return armourJumpGunAtlasConfig;
            case GUN_EQUIPPED:
                return jumpGunAtlasConfig;
            default:
                return jumpAtlasConfig;
        }
    }

    private Animation getJumpAnimation(final AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourJumpAnimation;
            case ARMOUR_AND_GUN_EQUIPPED:
                return armourJumpGunAnimation;
            case GUN_EQUIPPED:
                return jumpGunAnimation;
            default:
                return jumpAnimation;
        }
    }

    private void drawUpgradingAnimation(Batch batch, AnimationState animationState) {
        final Animation animation;
        final TextureRegion region;
        final PositionConfig positionConfig = this.runnerConfig.getPosition();
        final Vector2 defaultPosition = new Vector2(positionConfig.getX(), positionConfig.getY());
        final AtlasConfig atlasConfig = getAtlasConfig(animationState);

        this.setForcedPositionVector(Optional.of(defaultPosition));
        maintainPosition();
        animation = getUpgradingAnimation(animationState);
        region = animation.getKeyFrame(stateTime, true);
        drawAnimation(batch,
                region,
                Optional.of(atlasConfig.getImageOffset()),
                Optional.of(atlasConfig.getImageScale()));
    }

    private AtlasConfig getAtlasConfig(AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourRunGunAtlasConfig;
            default:
                return armourRunAtlasConfig;
        }
    }

    private Animation getUpgradingAnimation(final AnimationState animationState) {
        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourRunGunAnimation;
            case GUN_EQUIPPED:
                return runGunAnimation;
            default:
                return this.getDefaultAnimation();
        }
    }

    private Animation getSlideAnimation(final AnimationState animationState) {

        switch(animationState){
            case ARMOUR_EQUIPPED:
                return armourSlideAnimation;
            case ARMOUR_AND_GUN_EQUIPPED:
                return armourSlideGunAnimation;
            case GUN_EQUIPPED:
                return slideGunAnimation;
            default:
                return slideAnimation;
        }

    }

    public void resetPosition(){
        final Filter f = new Filter();
        f.categoryBits = Constants.RUNNER_RUNNING_CATEGORY;
        f.groupIndex = Constants.RUNNER_RUNNING_GROUP_INDEX;
        f.maskBits = Constants.RUNNER_RUNNING_MASK_INDEX;
        final Body body = this.getBody();
        if(body!=null) {
            final Array<Fixture> fixtureList = body.getFixtureList();
            if(fixtureList != null && fixtureList.size > 0) {
                fixtureList.get(0).setFilterData(f);
            }
            body.setTransform(Box2dFactory.getInstance().getRunPosition(), 0f);
        }
    }

    private void scheduleDeath(final Body body) {
        if(!deathScheduled) {
            final HitVectorConfig hitVectorConfig = runnerConfig.getHitVectorConfig();
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    final float hitVelocityX = hitVectorConfig.getHitVelocityX();
                    final float hitVelocityY = hitVectorConfig.getHitVelocityY();
                    final float hitImpulseX = hitVectorConfig.getHitImpulseX();
                    final float hitImpulseY = hitVectorConfig.getHitImpulseY();
                    final Filter f = createDeathCollisionFilter();
                    final Vector2 linearVelocity = new Vector2(hitVelocityX, hitVelocityY);
                    final Vector2 impulseVector = new Vector2(hitImpulseX, hitImpulseY);
                    final Optional<Float> rotationOptional = Optional.absent();
                    final CollisionParams collisionParams = new CollisionParams(
                            linearVelocity, impulseVector, f, body, rotationOptional);
                    applyCollisionImpulse(collisionParams);
                    clearTransform();
                }
            }, hitVectorConfig.getHitPauseTime());
            deathScheduled = true;
        }
    }

    @Subscribe
    public void onBossDead(BossDeadEvent bossDeadEvent){

        final PositionConfig endOfLevelPosition = this.runnerConfig.getEndOfLevelPosition();
        final float endVelocityX = this.runnerConfig.getEndOfLevelVelocityX();
        final float endVelocityY = this.runnerConfig.getEndOfLevelVelocityY();
        moveToPosition(endOfLevelPosition, endVelocityX, endVelocityY);

    }

    private void moveToPosition(PositionConfig position, float endVelocityX, float endVelocityY) {
        this.setEndOfLevelPosition(Optional.fromNullable(position));
        this.setForcedLinearVelocity(endVelocityX, endVelocityY);
        this.setAnimationState(AnimationState.DEFAULT);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(collidedWithDeadBoss){
            Events.get().post(new RunnerEndLevelEvent());
            Box2dFactory.destroyAndRemove(this);
        } else {
            if (endOfLevelPosition.isPresent()) {
                if (this.getBody().getPosition().x >= endOfLevelPosition.get().getX()) {
                    clearForcedLinearVelocity();
                    this.collidedWithDeadBoss = false;
                    this.destroyOnNextCollision = true;
                    jump(null);
                    //maintainPosition();
                }
            }
        }
    }

    private Filter createDeathCollisionFilter() {
        final Filter f = new Filter();
        f.categoryBits = Constants.RUNNER_HIT_CATEGORY;
        f.groupIndex = Constants.RUNNER_HIT_GROUP_INDEX;
        f.maskBits = Constants.RUNNER_HIT_MASK_INDEX;
        return f;
    }

    private void clearTransform() {
        final Optional<Vector2> noTransform = Optional.absent();
        this.setForcedTransform(noTransform);
    }

    private void drawSlideAnimation(Batch batch, TextureRegion textureRegion) {
        final Rectangle textureBounds = this.getTextureBounds();
        float x = textureBounds.x;
        float y = textureBounds.y;
        //swap width and height
        float width = textureBounds.getHeight();
        float height = textureBounds.getWidth();
        final Optional<ImageOffsetConfig> offsetOptional =
                Optional.fromNullable(this.slideAtlasConfig.getImageOffset());
        final Optional<ImageScaleConfig> scaleOptional =
                Optional.fromNullable(this.slideAtlasConfig.getImageScale());

        if(offsetOptional.isPresent()){
            final ImageOffsetConfig imageOffsetConfig = offsetOptional.get();
            x += imageOffsetConfig.getX();
            y += imageOffsetConfig.getY();
        }
        if(scaleOptional.isPresent()){
            final ImageScaleConfig imageScaleConfig = scaleOptional.get();
            width *= imageScaleConfig.getX();
            height *= imageScaleConfig.getY();
        }
        batch.draw(textureRegion, x, y, width, height);
    }


    @Subscribe
    public void beginContact(BeginContactEvent beginContactEvent){

        if(destroyOnNextCollision){
            collidedWithDeadBoss = true;
            return;
        } else {
            final Optional<RunnerActor> runnerActorOptional = beginContactEvent.getRunnerActor();
            final Optional<EnemyActor> enemyActorOptional = beginContactEvent.getEnemyActor();
            final Optional<GroundActor> groundActorOptional = beginContactEvent.getGroundActor();
            final Optional<ArmourActor> armourActorOptional = beginContactEvent.getArmourActor();
            final Optional<GunActor> gunActorOptional = beginContactEvent.getGunActor();
            for (RunnerActor runnerActor : runnerActorOptional.asSet()) {
                final ActorState actorState = this.getActorState();
                for (EnemyActor enemyActor : enemyActorOptional.asSet()) {
                    if (!actorState.equals(ActorState.HIT)) {
                        this.hit(enemyActor);
                    }
                }
                for (ArmourActor armourActor : armourActorOptional.asSet()) {
                    if (!actorState.equals(ActorState.HIT)) {
                        this.pickupArmour();
                    }
                }
                for (GunActor gunActor : gunActorOptional.asSet()) {
                    if (!actorState.equals(ActorState.HIT)) {
                        this.pickupGun();
                    }
                }
                for (GroundActor groundActor : groundActorOptional.asSet()) {
                    this.setLanded();
                }
            }
        }

    }

    private void pickupArmour() {
        Events.get().post(new ArmourPickedUpEvent());
        this.setActorState(ActorState.UPGRADING_ARMOUR);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(getAnimationState().equals(AnimationState.GUN_EQUIPPED)){
                    setAnimationState(AnimationState.ARMOUR_AND_GUN_EQUIPPED);
                } else {
                    setAnimationState(AnimationState.ARMOUR_EQUIPPED);
                }
                unMaintainPosition();
                clearTransform();
                setActorState(ActorState.RUNNING);
                resetPosition();
                getBody().setLinearVelocity(0f, 0f);
            }
        }, 0.1f);
    }

    private void pickupGun() {
        Events.get().post(new GunPickedUpEvent());
        this.setActorState(ActorState.UPGRADING_GUN);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(getAnimationState().equals(AnimationState.ARMOUR_EQUIPPED)) {
                    setAnimationState(AnimationState.ARMOUR_AND_GUN_EQUIPPED);
                } else {
                    setAnimationState(AnimationState.GUN_EQUIPPED);
                }
                unMaintainPosition();
                clearTransform();
                setActorState(ActorState.RUNNING);
                resetPosition();
                getBody().setLinearVelocity(0f, 0f);
            }
        }, 0.1f);
    }

    @Subscribe
    public void jump(JumpControlInvokedEvent jumpControlInvokedEvent) {
        if (canJump()) {
            if(this.getActorState().equals(ActorState.SLIDING)){
                this.stopSliding();
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        applyJump();
                        setActorState(ActorState.JUMPING);
                    }
                },0.1f);
            }else {
                applyJump();
                setActorState(ActorState.JUMPING);
            }
        }
    }

    private void applyJump() {
        final Body body = getBody();
        final Vector2 runnerLinerImpulse = Box2dFactory.getInstance().getRunnerJumpImpulse();
        body.applyLinearImpulse(runnerLinerImpulse,
                body.getWorldCenter(), true);
    }

    @Subscribe
    public void slide(SlideControlInvokedEvent slideControlInvokedEvent){
        if( canSlide() ){
            if(!getActorState().equals(ActorState.SLIDING)) {
                final Body body = getBody();
                body.setTransform(Box2dFactory.getInstance().getSlidePosition(),
                        Box2dFactory.getInstance().getSlideAngle());
                maybeUpdateTextureBounds();
                scheduleStopSliding();
                setActorState(ActorState.SLIDING);
            }
        }
    }

    private void scheduleStopSliding() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if (getActorState().equals(ActorState.SLIDING)) {
                    stopSliding();
                }
            }
        }, this.runnerConfig.getSlideTime());
    }

    public void stopSliding(){
        if(canSlide()) {
            final Body body = getBody();
            body.setTransform(Box2dFactory.getInstance().getRunPosition(), 0f);
            maybeUpdateTextureBounds();
            setActorState(ActorState.RUNNING);
        }
   }

    public void hit(final EnemyActor enemyActor) {

        if(enemyActor.getActorState().equals(ActorState.HIT_BY_ARMOUR))
            return;


        if(enemyActor instanceof EnemyBulletActor){
            if(getAnimationState().equals(AnimationState.ARMOUR_EQUIPPED)) {
                return;
            }
        }

        killRunner();

    }

    private void killRunner() {
        final Body body = getBody();
        setActorState(ActorState.HIT);
        scheduleDeath(body);
        Events.get().post(new RunnerHitEvent(this));
    }

    public boolean canJump(){
        final ActorState state = this.getActorState();
        return !(state.equals(ActorState.JUMPING) ||
                state.equals(ActorState.HIT) ||
                state.equals(ActorState.FALLING));
    }

    public boolean canSlide(){
        final ActorState state = this.getActorState();
        return !(state.equals(ActorState.JUMPING) ||
                state.equals(ActorState.HIT) ||
                state.equals(ActorState.FALLING));
    }

    public void setLanded() {
        final ActorState state = this.getActorState();
        if(!(state.equals(ActorState.HIT) || state.equals(ActorState.FALLING)))
            setActorState(ActorState.RUNNING);
    }

    public void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setEndOfLevelPosition(Optional<PositionConfig> endOfLevelPosition) {
        this.endOfLevelPosition = endOfLevelPosition;
    }

    public Optional<PositionConfig> getEndOfLevelPosition() {
        return endOfLevelPosition;
    }
}
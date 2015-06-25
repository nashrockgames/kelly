package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.ImageOffset;
import com.nrg.kelly.config.actors.ImageScale;
import com.nrg.kelly.events.ArmourPickedUpEvent;
import com.nrg.kelly.events.GameOverEvent;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.SlideControlInvokedEvent;
import com.nrg.kelly.events.screen.JumpControlInvokedEvent;
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
    private Runner runnerConfig;
    private AtlasConfig armourJumpAtlasConfig;
    private AtlasConfig armourSlideAtlasConfig;
    private AtlasConfig armourRunAtlasConfig;
    private Animation armourJumpAnimation;
    private Animation armourSlideAnimation;
    private Animation armourRunAnimation;

    public RunnerActor(Runner runner, CameraConfig cameraConfig) {
        super(runner, cameraConfig);
        this.runnerConfig = runner;
        Events.get().register(this);
        setWidth(runner.getWidth());
        setHeight(runner.getHeight());
        this.createTextures();
    }

    public void createTextures(){

        final List<AtlasConfig> atlasConfigList = runnerConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));
        jumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "jump");
        slideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "slide");
        dieAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "die");

        armourJumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-jump");
        armourSlideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-slide");
        armourRunAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "armour-run");


        final String run = getDefaultAtlasConfig().getAtlas();
        final String jump = jumpAtlasConfig.getAtlas();
        final String slide = slideAtlasConfig.getAtlas();
        final String die = dieAtlasConfig.getAtlas();
        final String armourRun = armourRunAtlasConfig.getAtlas();
        final String armourJump = armourJumpAtlasConfig.getAtlas();
        final String armourSlide = armourSlideAtlasConfig.getAtlas();

        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(run));
        final TextureAtlas jumpAtlas = new TextureAtlas(Gdx.files.internal(jump));
        final TextureAtlas slideAtlas = new TextureAtlas(Gdx.files.internal(slide));
        final TextureAtlas dieAtlas = new TextureAtlas(Gdx.files.internal(die));
        final TextureAtlas armourJumpAtlas = new TextureAtlas(Gdx.files.internal(armourJump));
        final TextureAtlas armourSlideAtlas = new TextureAtlas(Gdx.files.internal(armourSlide));
        final TextureAtlas armourRunAtlas = new TextureAtlas(Gdx.files.internal(armourRun));

        setDefaultAnimation(new Animation(runnerConfig.getFrameRate(), defaultAtlas.getRegions()));
        jumpAnimation = new Animation(runnerConfig.getFrameRate(), jumpAtlas.getRegions());
        slideAnimation = new Animation(runnerConfig.getFrameRate(), slideAtlas.getRegions());
        dieAnimation = new Animation(runnerConfig.getFrameRate(), dieAtlas.getRegions());
        armourJumpAnimation = new Animation(runnerConfig.getFrameRate(), armourJumpAtlas.getRegions());
        armourSlideAnimation = new Animation(runnerConfig.getFrameRate(), armourSlideAtlas.getRegions());
        armourRunAnimation = new Animation(runnerConfig.getFrameRate(), armourRunAtlas.getRegions());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final TextureRegion region;
        final ActorState state = this.getActorState();
        switch(state){
            case SLIDING:
                region = slideAnimation.getKeyFrame(stateTime, true);
                drawSlideAnimation(batch, region);
                break;
            case JUMPING:
                region = jumpAnimation.getKeyFrame(stateTime, true);
                drawAnimation(batch,
                        region,
                        Optional.of(jumpAtlasConfig.getImageOffset()),
                        Optional.of(jumpAtlasConfig.getImageScale()));
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
                drawDefaultAnimation(batch);
                break;
        }

    }

    public void resetPosition(){
        //
        final Filter f = new Filter();
        f.categoryBits = Constants.RUNNER_RUNNING_CATEGORY;
        f.groupIndex = Constants.RUNNER_RUNNING_CATEGORY;
        f.maskBits = Constants.RUNNER_RUNNING_MASK_INDEX;
        this.getBody().getFixtureList().get(0).setFilterData(f);
        this.getBody().setTransform(Box2dFactory.getInstance().getRunPosition(), 0f);
        this.setActorState(ActorState.RUNNING);

    }


    private void scheduleDeath(final Body body) {
        if(!deathScheduled) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    final Filter f = new Filter();
                    f.categoryBits = Constants.RUNNER_HIT_CATEGORY;
                    f.groupIndex = Constants.RUNNER_HIT_GROUP_INDEX;
                    f.maskBits = Constants.RUNNER_HIT_MASK_INDEX;
                    body.getFixtureList().get(0).setFilterData(f);
                    body.setLinearVelocity(runnerConfig.getHitVelocityX(),
                            runnerConfig.getHitVelocityY());
                    body.applyLinearImpulse(runnerConfig.getHitImpulseX(),
                            runnerConfig.getHitImpulseY(), body.getPosition().x,
                            body.getPosition().y, true);
                    setActorState(ActorState.FALLING);
                    clearTransform();
                }
            }, runnerConfig.getHitPauseTime());
            deathScheduled = true;
        }
    }

    private void clearTransform() {
        this.setTransform(null);
    }

    private void drawSlideAnimation(Batch batch, TextureRegion textureRegion) {
        final Rectangle textureBounds = this.getTextureBounds();
        float x = textureBounds.x;
        float y = textureBounds.y;
        //swap width and height
        float width = textureBounds.getHeight();
        float height = textureBounds.getWidth();
        final Optional<ImageOffset> offsetOptional =
                Optional.fromNullable(this.slideAtlasConfig.getImageOffset());
        final Optional<ImageScale> scaleOptional =
                Optional.fromNullable(this.slideAtlasConfig.getImageScale());

        if(offsetOptional.isPresent()){
            final ImageOffset imageOffset = offsetOptional.get();
            x += imageOffset.getX();
            y += imageOffset.getY();
        }
        if(scaleOptional.isPresent()){
            final ImageScale imageScale = scaleOptional.get();
            width *= imageScale.getX();
            height *= imageScale.getY();
        }
        batch.draw(textureRegion, x, y, width, height);
    }


    @Subscribe
    public void beginContact(BeginContactEvent beginContactEvent){

        final Optional<RunnerActor> runnerActorOptional = beginContactEvent.getRunnerActor();
        final Optional<EnemyActor> enemyActorOptional = beginContactEvent.getEnemyActor();
        final Optional<GroundActor> groundActorOptional = beginContactEvent.getGroundActor();
        final Optional<ArmourActor> armourActorOptional = beginContactEvent.getArmourActor();
        for(RunnerActor runnerActor : runnerActorOptional.asSet()){
            for(EnemyActor enemyActor : enemyActorOptional.asSet()){
                if(!this.getActorState().equals(ActorState.HIT)){
                    this.hit();
                }
            }
            for(ArmourActor enemyActor : armourActorOptional.asSet()){
                if(!this.getActorState().equals(ActorState.HIT)){
                    this.pickupArmour();
                }
            }
            for(GroundActor groundActor : groundActorOptional.asSet()){
                this.setLanded();
            }

        }

    }

    private void pickupArmour() {
        Events.get().post(new ArmourPickedUpEvent());
        //TODO:
        //make sure this runner stays where he is (set run position)
        //use a timer to set the "animation state" to ARMOUR_EQUIPPED
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
        body.applyLinearImpulse(Box2dFactory.getInstance().getRunnerLinerImpulse(),
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
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
               if(getActorState().equals(ActorState.SLIDING)){
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

    public void hit() {
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

}
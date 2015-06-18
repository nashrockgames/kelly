package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Timer;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.ImageOffset;
import com.nrg.kelly.config.actors.ImageScale;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.config.actors.Runner;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.physics.BeginContactEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.screen.LeftSideScreenTouchDownEvent;
import com.nrg.kelly.events.screen.RightSideScreenTouchDownEvent;
import com.nrg.kelly.physics.Box2dFactory;
import com.nrg.kelly.events.screen.LeftSideScreenTouchUpEvent;

import java.util.List;

import javax.inject.Inject;

public class RunnerActor extends GameActor {

    private Animation jumpAnimation;
    private Animation slideAnimation;
    private Animation dieAnimation;

    @Inject
    GameConfig gameConfig;
    @Inject
    Box2dFactory box2dFactory;

    private AtlasConfig jumpAtlasConfig;
    private AtlasConfig slideAtlasConfig;
    private AtlasConfig dieAtlasConfig;
    private boolean deathScheduled = false;
    private Runner runnerConfig;

    @Inject
    public RunnerActor(Runner runner, CameraConfig cameraConfig) {
        super(runner, cameraConfig);
        this.runnerConfig = runner;
        Events.get().register(this);
        setWidth(runner.getWidth());
        setHeight(runner.getHeight());
    }

    @Subscribe
    public void createTextures(PostBuildGameModuleEvent postBuildGameModuleEvent){
        final Runner runnerConfig = gameConfig.getActors().getRunner();
        final List<AtlasConfig> atlasConfigList = runnerConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));

        jumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "jump");
        slideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "slide");
        dieAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "die");

        final String run = getDefaultAtlasConfig().getAtlas();
        final String jump = jumpAtlasConfig.getAtlas();
        final String slide = slideAtlasConfig.getAtlas();
        final String die = dieAtlasConfig.getAtlas();

        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(run));
        final TextureAtlas jumpAtlas = new TextureAtlas(Gdx.files.internal(jump));
        final TextureAtlas slideAtlas = new TextureAtlas(Gdx.files.internal(slide));
        final TextureAtlas dieAtlas = new TextureAtlas(Gdx.files.internal(die));

        setDefaultAnimation(new Animation(runnerConfig.getFrameRate(), defaultAtlas.getRegions()));
        jumpAnimation = new Animation(runnerConfig.getFrameRate(), jumpAtlas.getRegions());
        slideAnimation = new Animation(runnerConfig.getFrameRate(), slideAtlas.getRegions());
        dieAnimation = new Animation(runnerConfig.getFrameRate(), dieAtlas.getRegions());

    }

    @Subscribe
    public void createBody(PostBuildGameModuleEvent postBuildGameModuleEvent) {
        final Runner runner = gameConfig.getActors().getRunner();
        final Body body = box2dFactory.createRunner();
        body.setUserData(this);
        setBody(body);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final TextureRegion region;
        final ActorState state = this.getState();
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
                break;
            case RUNNING:
                drawDefaultAnimation(batch);
                break;
        }

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
                    setState(ActorState.FALLING);
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
        final Contact contact = beginContactEvent.getContact();
        final Body bodyA = contact.getFixtureA().getBody();
        final Body bodyB = contact.getFixtureB().getBody();
        final Object userData = bodyB.getUserData();
        if(bodyA.equals(this.getBody())){
            if(userData !=null) {
                if(userData instanceof GroundActor){
                    this.setLanded();
                }
                if (userData instanceof EnemyActor){
                    this.hit();
                }
            }
        }
    }

    @Subscribe
    public void jump(RightSideScreenTouchDownEvent rightSideScreenTouchDownEvent) {
        if (canJump()) {
            final Body body = getBody();
            body.applyLinearImpulse(box2dFactory.getRunnerLinerImpulse(),
                    body.getWorldCenter(), true);
            setState(ActorState.JUMPING);
        }
    }

    @Subscribe
    public void slide(LeftSideScreenTouchDownEvent leftSideScreenTouchDownEvent){
        if( canSlide() ){
            final Body body = getBody();
            body.setTransform(box2dFactory.getSlidePosition(), box2dFactory.getSlideAngle());
            maybeUpdateTextureBounds();
            setState(ActorState.SLIDING);
        }
    }

    @Subscribe
    public void stopSliding(LeftSideScreenTouchUpEvent leftSideScreenTouchUpEvent){
        if(canSlide()) {
            final Body body = getBody();
            body.setTransform(box2dFactory.getRunPosition(), 0f);
            maybeUpdateTextureBounds();
            setState(ActorState.RUNNING);
        }
   }

    public void hit() {
        final Body body = getBody();
        scheduleDeath(body);
        Events.get().post(new RunnerHitEvent(this));
        setState(ActorState.HIT);
    }

    public boolean canJump(){
        final ActorState state = this.getState();
        return !(state.equals(ActorState.JUMPING) ||
                state.equals(ActorState.HIT) ||
                state.equals(ActorState.SLIDING) ||
                state.equals(ActorState.FALLING));
    }

    public boolean canSlide(){
        final ActorState state = this.getState();
        return !(state.equals(ActorState.JUMPING) ||
                state.equals(ActorState.HIT) ||
                state.equals(ActorState.FALLING));
    }

    public void setLanded() {
        final ActorState state = this.getState();
        if(!(state.equals(ActorState.HIT) || state.equals(ActorState.FALLING)))
            setState(ActorState.RUNNING);
    }

}
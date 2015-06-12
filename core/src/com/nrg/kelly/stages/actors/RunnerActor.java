package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.GameConfig;
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
    private boolean hit = false;
    private boolean jumping = false;
    private boolean sliding = false;


    @Inject
    GameConfig gameConfig;
    @Inject
    Box2dFactory box2dFactory;

    private Runner runnerConfig;
    private AtlasConfig jumpAtlasConfig;
    private AtlasConfig slideAtlasConfig;

    @Inject
    public RunnerActor(Runner runner) {
        super(runner);
        Events.get().register(this);
    }

    @Subscribe
    public void createTextures(PostBuildGameModuleEvent postBuildGameModuleEvent){
        runnerConfig = gameConfig.getActors().getRunner();

        final List<AtlasConfig> atlasConfigList = runnerConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));

        jumpAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "jump");
        slideAtlasConfig = this.getAtlasConfigByName(atlasConfigList, "slide");

        final String run = getDefaultAtlasConfig().getAtlas();
        final String jump = jumpAtlasConfig.getAtlas();
        final String slide = slideAtlasConfig.getAtlas();
        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(run));
        final TextureAtlas jumpAtlas = new TextureAtlas(Gdx.files.internal(jump));
        final TextureAtlas slideAtlas = new TextureAtlas(Gdx.files.internal(slide));
        setDefaultAnimation(new Animation(runnerConfig.getFrameRate(), defaultAtlas.getRegions()));
        jumpAnimation = new Animation(runnerConfig.getFrameRate(), jumpAtlas.getRegions());
        slideAnimation = new Animation(runnerConfig.getFrameRate(), slideAtlas.getRegions());

    }

    @Subscribe
    public void createBody(PostBuildGameModuleEvent postBuildGameModuleEvent) {
        final Runner runner = gameConfig.getActors().getRunner();
        final Body body = box2dFactory.createRunner();
        body.setUserData(this);
        setBody(body);
        setWidth(runner.getWidth());
        setHeight(runner.getHeight());
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        final TextureRegion region;
        if (sliding) {
            region = slideAnimation.getKeyFrame(stateTime, true);
            drawSlideAnimation(batch, region);
        } else if (jumping) {
            region = jumpAnimation.getKeyFrame(stateTime, true);
            drawAnimation(batch,
                    region,
                    Optional.of(jumpAtlasConfig.getImageOffset()),
                    Optional.of(jumpAtlasConfig.getImageScale()));
        }else {
            drawDefaultAnimation(batch);
        }
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
                    this.landed();
                }
                if (userData instanceof EnemyActor){
                    //this.hit();
                }
            }
        }
    }

    @Subscribe
    public void jump(RightSideScreenTouchDownEvent rightSideScreenTouchDownEvent) {
        if (!(jumping || sliding || hit)) {
            final Body body = getBody();
            body.applyLinearImpulse(box2dFactory.getRunnerLinerImpulse(),
                    body.getWorldCenter(), true);
            jumping = true;
        }
    }

    @Subscribe
    public void slide(LeftSideScreenTouchDownEvent leftSideScreenTouchDownEvent){
        if( !(jumping || hit) ){
            final Body body = getBody();
            body.setTransform(box2dFactory.getSlidePosition(), box2dFactory.getSlideAngle());
            maybeUpdateTextureBounds();
            sliding = true;
        }
    }

    @Subscribe
    public void stopSliding(LeftSideScreenTouchUpEvent leftSideScreenTouchUpEvent){
        if(!(hit || jumping)) {
            final Body body = getBody();
            body.setTransform(box2dFactory.getRunPosition(), 0f);
            maybeUpdateTextureBounds();
            sliding = false;
        }
   }

    public void hit() {
        final Body body = getBody();
        body.applyAngularImpulse(box2dFactory.getHitAngularImpulse(), true);
        Events.get().post(new RunnerHitEvent());
        hit = true;
    }

    public void landed() {
       jumping = false;
    }


}
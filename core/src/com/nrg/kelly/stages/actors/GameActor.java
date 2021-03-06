package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.base.Optional;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.ImageOffsetConfig;
import com.nrg.kelly.config.actors.ImageScaleConfig;

import java.util.List;

public abstract class GameActor extends Actor {

    protected Optional<ActorConfig> actorConfigOptional;
    private Body body;
    protected float stateTime;
    private Animation defaultAnimation;
    private AtlasConfig defaultAtlasConfig;
    private ActorState actorState = ActorState.RUNNING;
    private Optional<Vector2> forcedTransform = Optional.absent();
    private float forcedTransformAngle = 0f;
    private Optional<Vector2> forcedPositionVector = Optional.absent();
    protected CameraConfig cameraConfig;
    private Optional<Float> textureRotation = Optional.absent();
    private Float currentTextureRotation = 0f;
    private Float currentBodyRotation = 0f;
    private Rectangle textureBounds = new Rectangle();
    private Optional<Vector2> forcedLinearVelocity = Optional.absent();

    public CameraConfig getCameraConfig() {
        return cameraConfig;
    }

    public void setCameraConfig(CameraConfig cameraConfig) {
        this.cameraConfig = cameraConfig;
    }

    public Optional<Vector2> getForcedPositionVector() {
        return forcedPositionVector;
    }

    public void setForcedPositionVector(Optional<Vector2> forcedPositionVector) {
        this.forcedPositionVector = forcedPositionVector;
    }

    protected void setForcedTransform(Optional<Vector2> forcedTransform) {
        this.forcedTransform = forcedTransform;
    }

    public float getForcedTransformAngle() {
        return forcedTransformAngle;
    }

    public void setForcedTransformAngle(float forcedTransformAngle) {
        this.forcedTransformAngle = forcedTransformAngle;
    }

    private Optional<Vector2> getForcedTransform() {
        return forcedTransform;
    }

    protected void clearForcedTransform(){
        this.forcedTransform = Optional.absent();
    }

    public ActorState getActorState() {
        return actorState;
    }

    public Animation getDefaultAnimation() {
        return defaultAnimation;
    }

    public void setDefaultAnimation(Animation defaultAnimation) {
        this.defaultAnimation = defaultAnimation;
    }

    protected void drawDefaultTexture(Batch batch){
        if (getDefaultAnimation() != null) {
            final TextureRegion region =
                    getDefaultAnimation().getKeyFrame(stateTime, true);
            final AtlasConfig defaultAtlasConfig = getDefaultAtlasConfig();
            //final ImageOffsetConfig imageOffsetConfig = defaultAtlasConfig.getImageOffset();
            final ImageScaleConfig imageScaleConfig = defaultAtlasConfig.getImageScale();


            final float x = this.getTextureBounds().getX();//(Gdx.graphics.getWidth() / 2 + imageOffsetConfig.getX()) - (region.getRegionWidth() / 2);
            final float y = this.getTextureBounds().getY();//(Gdx.graphics.getHeight() / 2 + imageOffsetConfig.getY()) - (region.getRegionHeight() / 2);
            final float width = region.getRegionWidth() * imageScaleConfig.getX();
            final float height = region.getRegionHeight() * imageScaleConfig.getY();
            batch.draw(region, x, y, width, height);
        }
    }

    protected void drawDefaultAnimation(Batch batch) {
        if (getDefaultAnimation() != null) {
            final TextureRegion region =
                    getDefaultAnimation().getKeyFrame(stateTime, true);
            final AtlasConfig defaultAtlasConfig = getDefaultAtlasConfig();
            final ImageOffsetConfig imageOffsetConfig = defaultAtlasConfig.getImageOffset();
            final ImageScaleConfig imageScaleConfig = defaultAtlasConfig.getImageScale();
            drawAnimation(batch, region,
                    Optional.fromNullable(imageOffsetConfig),
                    Optional.fromNullable(imageScaleConfig));
        }
    }

    protected void unMaintainPosition(){
        final Optional<Vector2> absent = Optional.absent();
        this.setForcedPositionVector(absent);
    }

    protected void maintainPosition() {
        final Optional<Vector2> currentPositionVector = getForcedPositionVector();
        if (currentPositionVector.isPresent()) {
            this.setForcedTransformAngle(0f);
            this.setForcedTransform(currentPositionVector);
        } else {
            float currentX = this.getBody().getPosition().x;
            float currentY = this.getBody().getPosition().y;
            setForcedPositionVector(Optional.fromNullable(new Vector2(currentX, currentY)));
        }
    }

    protected void applyCollisionImpulse(CollisionParams params) {
        final Body paramsBody = params.getBody();
        paramsBody.getFixtureList().get(0).setFilterData(params.getFilter());
        paramsBody.setLinearVelocity(params.getLinearVelocity());
        paramsBody.applyLinearImpulse(params.getLinearImpulse(),
                paramsBody.getPosition(), true);
        for (final Float angle : params.getRotation().asSet()) {
            this.currentBodyRotation += angle;
            if(this.currentBodyRotation >= 360.0){
                this.currentBodyRotation = 0.0f;
            }
            paramsBody.setTransform(paramsBody.getPosition(), this.currentBodyRotation);
            this.setTextureRotation(params.getRotation());
        }
        setActorState(ActorState.FALLING);
    }

    public AtlasConfig getDefaultAtlasConfig() {
        return defaultAtlasConfig;
    }

    public void setDefaultAtlasConfig(AtlasConfig defaultAtlasConfig) {
        this.defaultAtlasConfig = defaultAtlasConfig;
    }

    public GameActor(ActorConfig actorConfigOptional, CameraConfig cameraConfig) {
        this.actorConfigOptional = Optional.fromNullable(actorConfigOptional);
        this.cameraConfig = cameraConfig;
    }

    public AtlasConfig getAtlasConfigByName(List<AtlasConfig> atlasConfigList, String name) {
        for (final AtlasConfig a : atlasConfigList) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown atlas name " + name);
    }

    protected void clearForcedLinearVelocity() {
        forcedLinearVelocity = Optional.absent();
    }

    public Optional<Vector2> getForcedLinearVelocity() {
        return forcedLinearVelocity;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getForcedTransform().isPresent()) {
            final Vector2 position = getForcedTransform().get();
            this.getBody().setTransform(position, this.getForcedTransformAngle());
        }
        if(forcedLinearVelocity.isPresent()){
            final Vector2 linearVelocity = forcedLinearVelocity.get();
            this.getBody().setLinearVelocity(linearVelocity);
        }

        maybeUpdateTextureBounds();
    }

    protected void maybeUpdateTextureBounds() {
        if (this.getActorConfigOptional().isPresent()) {
            final Body body = this.getBody();
            if (body != null) {
                final Object userData = body.getUserData();
                if (userData instanceof GameActor) {
                    updateTextureBounds(this.cameraConfig, body.getPosition());
                }
            }
        }
    }


    protected boolean isWithinLeftBounds(ActorConfig actorConfig) {
        return (this.getBody().getPosition().x + actorConfig.getWidth() / 2 > 0);
    }

    protected boolean isWithinBounds(ActorConfig actorConfig) {
        final CameraConfig cameraConfig = this.getCameraConfig();
        return isWithinWidth(actorConfig, cameraConfig, this.getBody()) &&
                isWithinHeight(actorConfig, cameraConfig, this.getBody());
    }

    private boolean isWithinHeight(ActorConfig actorConfig, CameraConfig cameraConfig, Body body){
        final Vector2 position = body.getPosition();
        final float y = position.y + (actorConfig.getHeight() / 2.0f);
        return y > 0 && y < cameraConfig.getViewportHeight();
    }

    private boolean isWithinWidth(ActorConfig actorConfig, CameraConfig cameraConfig, Body body){
        final Vector2 position = body.getPosition();
        final float x = position.x + (actorConfig.getWidth() / 2.0f);
        return x > 0 && x < cameraConfig.getViewportWidth();
    }


    protected void drawAnimation(Batch batch,
                                 TextureRegion textureRegion,
                                 Optional<ImageOffsetConfig> offsetOptional,
                                 Optional<ImageScaleConfig> scaleOptional) {

        float x = textureBounds.x;
        float y = textureBounds.y;
        float width = textureBounds.getWidth();
        float height = textureBounds.getHeight();

        for(final ImageOffsetConfig imageOffsetConfig : offsetOptional.asSet()){
            x += imageOffsetConfig.getX();
            y += imageOffsetConfig.getY();
        }
        for(final ImageScaleConfig imageScaleConfig : scaleOptional.asSet()){
            width *= imageScaleConfig.getX();
            height *= imageScaleConfig.getY();
        }

        if(this.textureRotation.isPresent()) {
            final Float rotationDelta = textureRotation.get();
            currentTextureRotation += rotationDelta;
            final float originX = width / 2.0f;
            final float originY = height / 2.0f;
            batch.draw(textureRegion, x, y, originX , originY, width, height,
                    1.0f, 1.0f, currentTextureRotation, false);
            if(currentTextureRotation >= 360f){
                currentTextureRotation = 0f;
            }
        }else {
            batch.draw(textureRegion, x, y, width, height);
        }
    }

    protected void updateTextureBounds(CameraConfig cameraConfig, Vector2 newPosition) {

        final Rectangle textureBounds = this.getTextureBounds();
        final float worldToScreenScale = cameraConfig.getWorldToScreenScale();
        final float bodyWidth = getWidth();
        final float bodyHeight = getHeight();
        final float xRatio = (newPosition.x - bodyWidth) / cameraConfig.getViewportWidth();
        final float screenX = Gdx.graphics.getWidth() * xRatio;

        textureBounds.x = screenX;
        textureBounds.y = transformToScreen(newPosition.y - bodyHeight / 2, worldToScreenScale);
        textureBounds.width = transformToScreen(bodyWidth, worldToScreenScale);
        textureBounds.height = transformToScreen(bodyHeight, worldToScreenScale);
    }

    protected float transformToScreen(float n, float scale) {
        return n * scale;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    public Rectangle getTextureBounds() {
        return textureBounds;
    }

    public void setTextureBounds(Rectangle textureBounds) {
        this.textureBounds = textureBounds;
    }

    public Optional<ActorConfig> getActorConfigOptional() {
        return actorConfigOptional;
    }

    public void drawFirstFrame(Batch batch) {

        final TextureRegion textureRegion = getDefaultAnimation().getKeyFrame(0f, true);
        drawAnimation(batch,
            textureRegion,
            Optional.of(defaultAtlasConfig.getImageOffset()),
            Optional.of(defaultAtlasConfig.getImageScale()));

    }

    public void setActorState(ActorState actorState) {
        this.actorState = actorState;
    }

    public void setTextureRotation(Optional<Float> textureRotation) {
        this.textureRotation = textureRotation;
    }

    public void setForcedLinearVelocity(float endVelocityX, float endVelocityY) {
        this.forcedLinearVelocity = Optional.of(new Vector2(endVelocityX, endVelocityY));
    }

    public boolean destroyOnEndLevel() {
        return true;
    }

}
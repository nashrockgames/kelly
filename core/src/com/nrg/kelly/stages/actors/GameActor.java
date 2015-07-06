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
import com.nrg.kelly.config.actors.ImageOffset;
import com.nrg.kelly.config.actors.ImageScale;

import java.util.List;

public abstract class GameActor extends Actor {

    protected Optional<ActorConfig> config;
    private Body body;
    protected float stateTime;
    private Animation defaultAnimation;
    private AtlasConfig defaultAtlasConfig;
    private ActorState actorState = ActorState.RUNNING;
    private Optional<Vector2> forcedTransform = Optional.absent();
    private float forcedTransformAngle = 0f;
    private Optional<Vector2> forcedPositionVector = Optional.absent();
    private CameraConfig cameraConfig;

    public CameraConfig getCameraConfig() {
        return cameraConfig;
    }

    public void setCameraConfig(CameraConfig cameraConfig) {
        this.cameraConfig = cameraConfig;
    }

    public Optional<Vector2> getForcedPositionVector() {
        return forcedPositionVector;
    }

    public void setForcedPositionVector(Optional<Vector2> hitVector) {
        this.forcedPositionVector = hitVector;
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
            final ImageOffset imageOffset = defaultAtlasConfig.getImageOffset();
            final ImageScale imageScale = defaultAtlasConfig.getImageScale();
            batch.draw(region,
                    (Gdx.graphics.getWidth() / 2 + imageOffset.getX()) - (region.getRegionWidth() / 2),
                    (Gdx.graphics.getHeight() / 2 + imageOffset.getY()) - (region.getRegionHeight() / 2),
                    region.getRegionWidth() * imageScale.getX(),
                    region.getRegionHeight() * imageScale.getY());
        }
    }

    protected void drawDefaultAnimation(Batch batch) {
        if (getDefaultAnimation() != null) {
            final TextureRegion region =
                    getDefaultAnimation().getKeyFrame(stateTime, true);
            final AtlasConfig defaultAtlasConfig = getDefaultAtlasConfig();
            final ImageOffset imageOffset = defaultAtlasConfig.getImageOffset();
            final ImageScale imageScale = defaultAtlasConfig.getImageScale();
            drawAnimation(batch, region,
                    Optional.fromNullable(imageOffset),
                    Optional.fromNullable(imageScale));
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


    public AtlasConfig getDefaultAtlasConfig() {
        return defaultAtlasConfig;
    }

    public void setDefaultAtlasConfig(AtlasConfig defaultAtlasConfig) {
        this.defaultAtlasConfig = defaultAtlasConfig;
    }

    private Rectangle textureBounds
            = new Rectangle();

    public GameActor(ActorConfig config, CameraConfig cameraConfig) {
        this.config = Optional.fromNullable(config);
        this.cameraConfig = cameraConfig;
    }

    public AtlasConfig getAtlasConfigByName(List<AtlasConfig> atlasConfigList, String name) {
        for (AtlasConfig a : atlasConfigList) {
            if (a.getName().equals(name)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown atlas name " + name);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getForcedTransform().isPresent()) {
            final Vector2 position = getForcedTransform().get();
            this.getBody().setTransform(position, this.getForcedTransformAngle());
        }
        maybeUpdateTextureBounds();
    }

    protected void maybeUpdateTextureBounds() {
        if (this.getConfig().isPresent()) {
            final Body body = this.getBody();
            if (body != null) {
                final Object userData = body.getUserData();
                if (userData instanceof GameActor) {
                    updateTextureBounds(this.cameraConfig);
                }
            }
        }
    }

    protected void drawAnimation(Batch batch,
                                 TextureRegion textureRegion,
                                 Optional<ImageOffset> offsetOptional,
                                 Optional<ImageScale> scaleOptional) {

        float x = textureBounds.x;
        float y = textureBounds.y;
        float width = textureBounds.getWidth();
        float height = textureBounds.getHeight();

        if (offsetOptional.isPresent()) {
            final ImageOffset imageOffset = offsetOptional.get();
            x += imageOffset.getX();
            y += imageOffset.getY();
        }
        if (scaleOptional.isPresent()) {
            final ImageScale imageScale = scaleOptional.get();
            width *= imageScale.getX();
            height *= imageScale.getY();
        }

        batch.draw(textureRegion, x, y, width, height);
    }


    protected void updateTextureBounds(CameraConfig cameraConfig) {
        //get the screen width
        final Rectangle textureBounds = this.getTextureBounds();
        final float worldToScreenScale = cameraConfig.getWorldToScreenScale();
        final float bodyWidth = getWidth();
        final float bodyHeight = getHeight();
        textureBounds.x = transformToScreen(getBody().getPosition().x - bodyWidth / 2, worldToScreenScale);
        textureBounds.y = transformToScreen(getBody().getPosition().y - bodyHeight / 2, worldToScreenScale);
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

    public Optional<ActorConfig> getConfig() {
        return config;
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
}
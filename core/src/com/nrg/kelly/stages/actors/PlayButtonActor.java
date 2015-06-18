package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.buttons.PlayButtonConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;

import java.util.List;

import javax.inject.Inject;

public class PlayButtonActor extends GameActor{

    @Inject
    GameConfig gameConfig;

    @Inject
    public PlayButtonActor(PlayButtonConfig config, CameraConfig cameraConfig) {
        super(config, cameraConfig);
        Events.get().register(this);
        setWidth(config.getWidth());
        setHeight(config.getHeight());
    }

    @Subscribe
    public void createTextures(PostBuildGameModuleEvent postBuildGameModuleEvent) {
        final PlayButtonConfig playButtonConfig = gameConfig.getActors().getPlayButton();
        final List<AtlasConfig> atlasConfigList = playButtonConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));
        final String play = getDefaultAtlasConfig().getAtlas();
        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(play));
        setDefaultAnimation(new Animation(playButtonConfig.getFrameRate(), defaultAtlas.getRegions()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        drawDefaultTexture(batch);
    }

}

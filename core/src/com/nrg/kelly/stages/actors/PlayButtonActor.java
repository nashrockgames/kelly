package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.buttons.PlayButtonConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;

import java.util.List;

import javax.inject.Inject;

public class PlayButtonActor extends GameActor{

    @Inject
    GameConfig gameConfig;

    @Inject
    GameStateManager gameStateManager;

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

    @Subscribe
    public void onPlayButtonPressed(PlayButtonClickedEvent playButtonClickedEvent){

        gameStateManager.setGameState(GameState.PLAYING);
        //TODO: make this move down a few pixels before remove to make it look like a real button
        remove();

    }

    @Override
    public boolean destroyOnEndLevel(){
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        stateTime += Gdx.graphics.getDeltaTime();
        drawDefaultTexture(batch);
    }

}

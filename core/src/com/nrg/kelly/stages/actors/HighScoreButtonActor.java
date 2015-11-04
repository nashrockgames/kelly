package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.actors.ActorConfig;
import com.nrg.kelly.config.actors.AtlasConfig;
import com.nrg.kelly.config.actors.PositionConfig;
import com.nrg.kelly.config.buttons.HighScoreButtonConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostBuildGameModuleEvent;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Andrew on 22/10/2015.
 */
public class HighScoreButtonActor extends GameActor{

    @Inject
    GameConfig gameConfig;

    @Inject
    GameStateManager gameStateManager;

    @Inject
    public HighScoreButtonActor(HighScoreButtonConfig config, CameraConfig cameraConfig) {
        super(config, cameraConfig);
        Events.get().register(this);
        setWidth(config.getWidth());
        setHeight(config.getHeight());
    }

    @Subscribe
    public void createTextures(PostBuildGameModuleEvent postBuildGameModuleEvent) {
        final HighScoreButtonConfig highScoreButtonConfig = gameConfig.getActors().getHighScoreButton();
        final List<AtlasConfig> atlasConfigList = highScoreButtonConfig.getAnimations();
        setDefaultAtlasConfig(this.getAtlasConfigByName(atlasConfigList, "default"));
        final String highScore = getDefaultAtlasConfig().getAtlas();
        final TextureAtlas defaultAtlas = new TextureAtlas(Gdx.files.internal(highScore));
        setDefaultAnimation(new Animation(highScoreButtonConfig.getFrameRate(), defaultAtlas.getRegions()));
        for(ActorConfig actorConfig : this.actorConfigOptional.asSet()){
            final PositionConfig position = actorConfig.getPosition();
            updateTextureBounds(cameraConfig, new Vector2(position.getX(), position.getY()));
        }

    }
/*
    @Subscribe
    public void onPlayButtonPressed(PlayButtonClickedEvent playButtonClickedEvent){

        gameStateManager.setGameState(GameState.PLAYING);
        //TODO: make this move down a few pixels before remove to make it look like a real button
        remove();

    }
*/
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

package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.FontManager;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;
import com.nrg.kelly.config.CameraConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.RunnerHitEvent;
import com.nrg.kelly.events.screen.PlayButtonClickedEvent;

import javax.inject.Inject;


public class ScoreActor extends Actor {

    private float score;
    private int multiplier;
    private Rectangle bounds;
    private BitmapFont font;

    @Inject
    FontManager fontManager;

    @Inject
    GameStateManager gameStateManager;

    private boolean runnerHit = false;

    @Inject
    public ScoreActor(CameraConfig cameraConfig) {
        Events.get().register(this);

        final int worldToScreenScale = cameraConfig.getWorldToScreenScale();
        final float width = 2f * worldToScreenScale;
        final float height = 0.5f * worldToScreenScale;
        final float y = (cameraConfig.getViewportHeight() - 2.0f) * worldToScreenScale;
        final float x = 0.25f * worldToScreenScale;
        bounds = new Rectangle(x, y, width , height);
        setWidth(width);
        setHeight(height);
        score = 0;
        multiplier = 5;
    }

    @Subscribe
    public void onRunnerHit(RunnerHitEvent runnerHitEvent){
        this.runnerHit = true;
    }

    @Subscribe
    public void onRunnerHit(PlayButtonClickedEvent playButtonClickedEvent){
        this.runnerHit = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        final GameState gameState = gameStateManager.getGameState();
        if (runnerHit || gameState != GameState.PLAYING) {
            return;
        }
        score += multiplier * delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(font == null ){
            font = fontManager.getMediumFont();
        }
        font.draw(batch, String.format("Score: %d", getScore()), bounds.x, bounds.y, bounds.width, Align.left, true);

    }

    public int getScore() {
        return (int) Math.floor(score);
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

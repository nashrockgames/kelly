package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.Gdx;
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

    private static float margin = 0.55f;

    @Inject
    public ScoreActor(CameraConfig cameraConfig) {
        Events.get().register(this);

        final int worldToScreenScale = cameraConfig.getWorldToScreenScale();
        //final float x = 7.5f * worldToScreenScale;
        //final float y = 11f * worldToScreenScale;
        //final float height = 2f * worldToScreenScale;
        //final float width = 8f * worldToScreenScale;



        final int aGdxgraphicsWidth = Gdx.graphics.getWidth();
        final int aGdxgraphicsHeight = Gdx.graphics.getHeight();
        final float x = aGdxgraphicsWidth - (aGdxgraphicsWidth * margin);
        final float y = aGdxgraphicsHeight - (aGdxgraphicsHeight * 0.02f);
        final float width = aGdxgraphicsWidth/2f;
        final float height = 32f;
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
        font.draw(batch, String.format("%d", getScore()), bounds.x, bounds.y, bounds.width, Align.right, true);

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

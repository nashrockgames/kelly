package com.nrg.kelly.stages.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.nrg.kelly.FontManager;
import com.nrg.kelly.GameState;
import com.nrg.kelly.GameStateManager;

import javax.annotation.PostConstruct;
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

    public ScoreActor(Rectangle bounds) {
        this.bounds = bounds;
        setWidth(bounds.width);
        setHeight(bounds.height);
        score = 0;
        multiplier = 5;

    }

    @PostConstruct
    public void postConstruct(){
        font = fontManager.getSmallFont();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (gameStateManager.getGameState() != GameState.PLAYING) {
            return;
        }
        score += multiplier * delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (getScore() == 0) {
            return;
        }
        //draw(Batch batch, java.lang.CharSequence str, float x, float y, float targetWidth, int halign, boolean wrap)
        font.draw(batch, String.format("%d", getScore()), bounds.x, bounds.y, bounds.width, Align.left, true);
    }

    public int getScore() {
        return (int) Math.floor(score);
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }
}

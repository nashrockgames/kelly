package com.nrg.kelly.stages.text;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nrg.kelly.events.DisposeGameEvent;
import com.nrg.kelly.events.Events;
import com.squareup.otto.Subscribe;

/**
 * Created by Andrew on 3/05/2015.
 */
public abstract class TextActor extends Actor {

    private SpriteBatch batch;
    private BitmapFont font;
    public String text = ">";

    public TextActor(){
        Events.get().register(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void create(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.RED);
    }

    @Subscribe
    public void dispose(DisposeGameEvent disposeGameEvent) {
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}

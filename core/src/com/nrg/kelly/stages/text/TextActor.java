package com.nrg.kelly.stages.text;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.game.DisposeGameEvent;
import com.nrg.kelly.events.Events;


/**
 * Created by Andrew on 3/05/2015.
 */
public abstract class TextActor extends Actor {

    private BitmapFont font;
    public String text = "Here is some text";

    public TextActor(){
        font = new BitmapFont(Gdx.files.internal("Gunsuh-Yellow-32.fnt"), false);
        Events.get().register(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Subscribe
    public void dispose(DisposeGameEvent disposeGameEvent) {
        font.dispose();
    }

    public BitmapFont getFont() {
        return font;
    }

    public void setFont(BitmapFont font) {
        this.font = font;
    }
}

package com.nrg.kelly.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostConstructGameEvent;
import com.nrg.kelly.events.game.PreConstructGameEvent;

import java.lang.ref.PhantomReference;

import javax.inject.Inject;

/**
 * Created by Andrew on 11/05/2015.
 */
public class MainMenuScreenView {

    private BitmapFont bitmapFont;
    private Pixmap pixmap;

    private final static String FONT_PATH = "Gunsuh-Yellow-32.fnt";
    private Texture texture;
    private Skin skin;

    @Inject
    public MainMenuScreenView(){

    }

    @Subscribe
    public void initialise(PreConstructGameEvent postConstructGameEvent){

        this.bitmapFont = new BitmapFont(Gdx.files.internal(FONT_PATH));
        this.pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(Color.WHITE);
        this.texture = new Texture(this.pixmap);
        this.skin = new Skin();

    }



}

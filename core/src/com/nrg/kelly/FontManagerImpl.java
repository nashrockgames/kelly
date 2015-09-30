package com.nrg.kelly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.fonts.FontConfig;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.events.game.PostCreateGameEvent;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class FontManagerImpl implements FontManager {

    private BitmapFont mediumFont;

    GameConfig gameConfig;

    public FontManagerImpl(GameConfig gameConfig){
        this.gameConfig = gameConfig;
        Events.get().register(this);
    }

    @Subscribe
    public void postConstruct(PostCreateGameEvent postCreateGameEvent){
        final FontConfig mediumFont = gameConfig.getFonts().getMediumFont();
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(mediumFont.getName()));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = mediumFont.getSize();
        this.mediumFont = generator.generateFont(parameter);
        this.mediumFont.setColor(mediumFont.getRed(), mediumFont.getGreen(), mediumFont.getBlue(), mediumFont.getAlpha());
    }

    @Override
    public BitmapFont getMediumFont() {
        return mediumFont;
    }
}

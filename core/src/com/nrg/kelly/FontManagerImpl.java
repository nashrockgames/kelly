package com.nrg.kelly;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.game.PostCreateGameEvent;

public class FontManagerImpl implements FontManager {

    private BitmapFont smallFont;

    @Subscribe
    public void postConstruct(PostCreateGameEvent postCreateGameEvent){
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(Constants.FONT_NAME));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 36;
        smallFont = generator.generateFont(parameter);
        smallFont.setColor(.21f, .22f, .21f, 1f);
    }

    @Override
    public BitmapFont getSmallFont() {
        return smallFont;
    }
}

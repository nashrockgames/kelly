package com.nrg.kelly.config.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.config.menus.buttons.Button;
import com.nrg.kelly.config.messages.MessagesFactory;
import com.nrg.kelly.util.Consumer;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by Andrew on 18/05/2015.
 */
public class MenuButtonConfigConsumer implements Consumer<Button> {

    private final Skin skin;
    private final String menuId;

    public MenuButtonConfigConsumer(Menu menu){
        final MenuButtonFactory menuButtonFactory = MenuButtonFactory.getInstance();
        final Map<String, Map<String,TextButton>> menuButtons = menuButtonFactory.getMenuButtons();
        menuButtons.put(menu.getId(), new HashMap<String, TextButton>());
        this.menuId = menu.getId();
        this.skin = new Skin(Gdx.files.internal(menu.getSkin()),
                new TextureAtlas(Gdx.files.internal(menu.getAtlas())));

    }

    @Override
    public void accept(Button button) {

        final String buttonLabel = MessagesFactory.getButtonLabel(menuId, button.getId());
        final MenuButtonFactory menuButtonFactory = MenuButtonFactory.getInstance();
        final Map<String, Map<String, TextButton>> menuButtons = menuButtonFactory.getMenuButtons();
        final Map<String, TextButton> textButtonMap = menuButtons.get(menuId);
        textButtonMap.put(button.getId(), new TextButton(buttonLabel, this.skin));

    }
}

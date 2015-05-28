package com.nrg.kelly.config.factories;

import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.config.menus.buttons.Button;
import com.nrg.kelly.util.Consumer;

import java.util.List;


/**
 * Created by Andrew on 18/05/2015.
 */
public class MenuConfigConsumer implements Consumer<Menu> {

    @Override
    public void accept(Menu menu) {
        final List<Button> buttons = menu.getButtons();
        final MenuButtonConfigConsumer menuButtonConfigConsumer = new MenuButtonConfigConsumer(menu);
        for(Button b : buttons){
            menuButtonConfigConsumer.accept(b);
        }
    }
}

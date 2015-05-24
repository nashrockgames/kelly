package com.nrg.kelly.config.factories;

import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.config.menus.buttons.Button;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;


/**
 * Created by Andrew on 18/05/2015.
 */
public class MenuConfigConsumer implements Consumer<Menu> {

    @Override
    public void accept(Menu menu) {
        final List<Button> buttons = menu.getButtons();
        buttons.forEach(new MenuButtonConfigConsumer(menu));
    }
}

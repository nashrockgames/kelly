package com.nrg.kelly.config.messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.nrg.kelly.config.messages.menus.Menu;
import com.nrg.kelly.config.messages.menus.buttons.Button;

/**
 * Created by Andrew on 19/05/2015.
 */
public class MessagesFactory {

    private static Messages messages;

    public static Messages getMessages(){
        if(messages == null)
            messages = loadMessages();
        return messages;
    }

    private static Messages loadMessages(){
        final Json json  = new Json();
        //TODO: locale
        final FileHandle file = Gdx.files.internal("messages.json");
        return json.fromJson(Messages.class, file);
    }

    public static String getButtonLabel(final String menuId, final String buttonId){

        final Menu next = getMenu(menuId);

        final Iterable<Button> buttons = Iterables.filter(next.getButtons(),
            new Predicate<Button>() {
                @Override
                public boolean apply(Button input) {
                    return input.getId().equals(buttonId);
                }
            });

        return buttons.iterator().next().getLabel();

    }

    private static Menu getMenu(final String menuId) {
        final Iterable<Menu> menus = Iterables.filter(getMessages().getMenus(),
                new Predicate<Menu>() {
                    @Override
                    public boolean apply(Menu input) {
                        return input.getId().equals(menuId);
                    }
                });

        return menus.iterator().next();
    }


}

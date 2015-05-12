package com.nrg.kelly.screens;

import com.badlogic.gdx.Screen;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.events.game.PostConstructGameEvent;

import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

/**
 * Created by Andrew on 11/05/2015.
 */
public class MainMenuScreen implements Screen{


    @Inject
    MainMenuScreenView mainMenuScreenView;

    @Inject
    Config config;

    @Inject
    public MainMenuScreen(){

    }

    @Subscribe
    public void setup(PostConstructGameEvent postConstructGameEvent){

        final List<Menu> menus = config.getMenus();
        final Iterable<Menu> mainMenu =
                Iterables.filter(menus, new MenuIdFilter(Constants.MENU_IDS.MAIN));

        mainMenu.iterator().forEachRemaining(new Consumer<Menu>() {
            @Override
            public void accept(Menu menu) {
                //TODO: add the buttons
            }
        });



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

package com.nrg.kelly.screens.menus.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.Constants;
import com.nrg.kelly.events.menus.ButtonClickedEvent;
import com.nrg.kelly.events.Events;

import javax.inject.Inject;

public class MainMenuScreen implements Screen{


    @Inject
    MainMenuScreenView mainMenuScreenView;

    @Inject
    public MainMenuScreen(){
        Events.get().register(this);
    }

    @Subscribe
    public void buttonClicked(ButtonClickedEvent buttonClickedEvent){

        final Constants.BUTTON_ID buttonId = buttonClickedEvent.getButtonId();

        switch(buttonId){
            case MAIN_MENU_QUIT:
                Gdx.app.exit();
                break;
            default:
                break;
        }
    }

    @Override
    public void show() {
        mainMenuScreenView.show();
    }

    @Override
    public void render(float delta) {
        this.mainMenuScreenView.render(delta);
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

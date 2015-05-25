package com.nrg.kelly;

import com.badlogic.gdx.Game;
import com.google.common.eventbus.Subscribe;
import com.nrg.kelly.events.menus.ButtonClickedEvent;
import com.nrg.kelly.events.Events;
import com.nrg.kelly.screens.game.GameScreen;
import com.nrg.kelly.screens.menus.main.MainMenuScreen;

import javax.inject.Inject;

/**
 * Created by Andrew on 29/04/2015.
 */
public class KellyGame extends Game {

    @Inject
    MainMenuScreen mainMenuScreen;

    @Inject
    GameScreen gameScreen;

    @Inject
    public KellyGame(){
        Events.get().register(this);
    }

    @Override
    public void create() {
        this.setScreen(mainMenuScreen);
    }

    @Subscribe
    public void buttonClicked(ButtonClickedEvent buttonClickedEvent){
        final Constants.BUTTON_ID buttonId = buttonClickedEvent.getButtonId();
        switch(buttonId){
            case MAIN_MENU_PLAY:
                mainMenuScreen.dispose();
                this.setScreen(gameScreen);
                break;
            default:
                break;
        }
    }




}

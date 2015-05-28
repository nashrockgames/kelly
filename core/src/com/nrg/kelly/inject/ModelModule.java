package com.nrg.kelly.inject;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.GameConfig;
import com.nrg.kelly.config.factories.MenuButtonFactory;
import com.nrg.kelly.config.factories.MenuConfigConsumer;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.screens.menus.main.MainMenuScreenModel;
import com.nrg.kelly.stages.Box2dGameModel;
import com.nrg.kelly.stages.actors.RunnerActor;

import java.util.List;
import java.util.Map;


import dagger.Module;
import dagger.Provides;

@Module
public class ModelModule {

    @Provides
    Box2dGameModel provideGameStageModel(RunnerActor runnerActor
                                         ){
        Box2dGameModel gameStageModel = new Box2dGameModel();
        gameStageModel.getActors().add(runnerActor);
        return gameStageModel;
    }

    @Provides
    MenuButtonFactory provideMenuButtonFactory(GameConfig gameConfig){

        final MenuButtonFactory menuButtonFactory = MenuButtonFactory.getInstance();

        if(menuButtonFactory.isConfigured()) {
            return menuButtonFactory;
        } else {
            final List<Menu> menus = gameConfig.getMenus();
            final MenuConfigConsumer menuConfigConsumer = new MenuConfigConsumer();
            for(Menu menu : menus){
                menuConfigConsumer.accept(menu);
            }
            return menuButtonFactory;
        }

    }

    @Provides
    MainMenuScreenModel provideMainMenuScreenModel(MenuButtonFactory menuButtonFactory){
        final MainMenuScreenModel mainMenuScreenModel = new MainMenuScreenModel();
        final Map<String, TextButton> menuButtons = menuButtonFactory.getMenuButtons(Constants.MENU_ID.MAIN);
        mainMenuScreenModel.getTextButtons().putAll(menuButtons);
        return mainMenuScreenModel;
    }


}

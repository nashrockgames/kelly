package com.nrg.kelly.inject;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.nrg.kelly.Constants;
import com.nrg.kelly.config.Config;
import com.nrg.kelly.config.factories.MenuButtonFactory;
import com.nrg.kelly.config.factories.MenuConfigConsumer;
import com.nrg.kelly.config.menus.Menu;
import com.nrg.kelly.screens.MainMenuScreenModel;
import com.nrg.kelly.stages.Box2dGameModel;
import com.nrg.kelly.stages.actors.GroundActor;
import com.nrg.kelly.stages.actors.RunnerActor;
import com.nrg.kelly.stages.text.DebugText;

import java.util.List;
import java.util.Map;


import dagger.Module;
import dagger.Provides;

/**
 * Created by Andrew on 10/05/2015.
 */
@Module
public class ModelModule {

    @Provides
    Box2dGameModel provideGameStageModel(RunnerActor runnerActor,
                                         GroundActor groundActor){
        Box2dGameModel gameStageModel = new Box2dGameModel();
        gameStageModel.getActors().add(runnerActor);
        gameStageModel.getActors().add(groundActor);
        gameStageModel.getActors().add(new DebugText());
        return gameStageModel;
    }

    @Provides
    MenuButtonFactory provideMenuButtonFactory(Config config){

        final MenuButtonFactory menuButtonFactory = MenuButtonFactory.getInstance();

        if(menuButtonFactory.isConfigured()) {
            return menuButtonFactory;
        } else {
            final List<Menu> menus = config.getMenus();
            menus.forEach(new MenuConfigConsumer());
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

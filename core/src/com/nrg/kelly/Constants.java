package com.nrg.kelly;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Constants {

    public static final int APP_WIDTH = 1024;
    public static final int APP_HEIGHT = 720;

    public static final short RUNNER_HIT_CATEGORY = 1;
    public static final short RUNNER_HIT_GROUP_INDEX = 2;
    public static final short RUNNER_HIT_MASK_INDEX = 0;
    public static final short RUNNER_RUNNING_CATEGORY = 1;
    public static final short RUNNER_RUNNING_GROUP_INDEX = 0;
    public static final short RUNNER_RUNNING_MASK_INDEX = -1;
    public static final short ENEMY_ARMOUR_HIT_CATEGORY = 2;
    public static final short ENEMY_ARMOUR_HIT_GROUP_INDEX = 3;
    public static final short ENEMY_ARMOUR_HIT_MASK_INDEX = 1;
    public static final short BOSS_DEATH_HIT_CATEGORY = 3;
    public static final short BOSS_DEATH_HIT_GROUP_INDEX = 1;
    public static final short BOSS_DEATH_HIT_MASK_INDEX = 1;


    public enum MENU_ID {
        MAIN("main_menu");
        private String id;
        MENU_ID(String id){
            this.id = id;
        }
        @Override
        public String toString(){
            return this.id;
        }
    }
    public enum BUTTON_ID {
        MAIN_MENU_QUIT,
        MAIN_MENU_PLAY
    }

}

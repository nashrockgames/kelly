package com.nrg.kelly;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Constants {

    public static final int APP_WIDTH = 640;
    public static final int APP_HEIGHT = 480;

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

package com.nrg.kelly;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Andrew on 26/04/2015.
 */
public class Constants {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;

    public enum MENU_IDS {
        MAIN("main_menu");
        private String id;
        private MENU_IDS(String id){
            this.id = id;
        }
        @Override
        public String toString(){
            return this.id;
        }
    }

   }

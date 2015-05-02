package com.nrg.kelly.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nrg.kelly.Constants;
import com.nrg.kelly.inject.DaggerAdapter;


public class DesktopLauncher {
	public static void main (String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.APP_WIDTH;
        config.height = Constants.APP_HEIGHT;
        new LwjglApplication(new DaggerAdapter(), config);
	}
}

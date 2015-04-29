package com.nrg.kelly.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nrg.dagger.DaggerAdapter;
import com.nrg.kelly.Constants;
import com.nrg.kelly.Main;
import com.nrg.kelly.inject.GameModule;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.APP_WIDTH;
        config.height = Constants.APP_HEIGHT;
        final DaggerAdapter daggerAdapter = new DaggerAdapter(Main.class, new GameModule());
		new LwjglApplication(daggerAdapter, config);
	}
}

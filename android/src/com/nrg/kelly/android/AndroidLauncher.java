package com.nrg.kelly.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.nrg.dagger.DaggerAdapter;
import com.nrg.kelly.Main;
import com.nrg.kelly.inject.GameModule;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        final DaggerAdapter daggerAdapter = new DaggerAdapter(Main.class, new GameModule());
		initialize(daggerAdapter, config);
	}
}

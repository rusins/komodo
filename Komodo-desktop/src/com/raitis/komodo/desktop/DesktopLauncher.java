package com.raitis.komodo.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.raitis.komodo.Komodo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Komodo";
		config.width = 640;
		config.height = 480;
		new LwjglApplication(new Komodo(), config);
	}
}

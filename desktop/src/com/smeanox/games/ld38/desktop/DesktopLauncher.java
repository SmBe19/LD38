package com.smeanox.games.ld38.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smeanox.games.ld38.Consts;
import com.smeanox.games.ld38.LD38;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Consts.DESIGN_WIDTH;
		config.height = Consts.DESIGN_HEIGHT;
		config.title = Consts.GAME_NAME;
		new LwjglApplication(new LD38(), config);
	}
}

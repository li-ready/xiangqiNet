package com.mygdx.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.IOException;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(120);
		config.setTitle("My GDX Game");
		//config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
		xiangqiNet game=new xiangqiNet();
		NetManagerC managerC= null;
		managerC = new NetManagerC(game);
		game.setManagerC(managerC);
		new Lwjgl3Application(game, config);
	}
}
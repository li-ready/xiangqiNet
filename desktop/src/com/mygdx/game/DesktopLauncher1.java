package com.mygdx.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationBase;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Cursor;
import com.mygdx.game.Gameinstance.GameSet;

import javax.swing.*;
import java.awt.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher1{
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(120);
		config.setTitle("chinesechess");
		//config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);

		Lwjgl3ApplicationConfiguration config0 = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(120);
		config.setTitle("chess");
		XiangqiNetStore store=new XiangqiNetStore();
		//config.setInitialVisible(false);
		boolean zhoumu=false;
		exittime exit1=new exittime();
		while(exit1.hh) {
			DengLu ui = new DengLu(zhoumu,exit1);
			ui.setGame(store);
			new Lwjgl3Application(ui, config);
			if(store.game!=null) {
				new Lwjgl3Application(store.game, config);
			}
			zhoumu=true;
		}
		System.out.println("successss eixt");
	}
}
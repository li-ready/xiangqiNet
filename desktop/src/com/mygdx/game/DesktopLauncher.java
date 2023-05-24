package com.mygdx.game;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		/*Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("chinesechess");
		//config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
		DengLu ui=new DengLu();
		new Lwjgl3Application(ui, config);*/
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(120);
		config.setTitle("My GDX Game");
		//config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
		/*File file=new File("D:/ah/sss/save/20230520210547.txt");
		FileReader fileReader;
		BufferedReader bufferedReader;
		String s="o";
		try {
			fileReader=new FileReader(file);
			bufferedReader=new BufferedReader(fileReader);
			s=bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		xiangqiNet game=new xiangqiNet();
		NetManagerC managerC= null;
		managerC = new NetManagerC(game);
		game.setManagerC(managerC);
		new Lwjgl3Application(game, config);
	}
}
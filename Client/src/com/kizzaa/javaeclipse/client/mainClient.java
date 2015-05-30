package com.kizzaa.javaeclipse.client;

import com.esotericsoftware.kryonet.*;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class mainClient extends StateBasedGame{
	
	public static Client client;
	public static Boolean connected = false;
	
	public static Image loginBtn;
	public static Image background;
	
	public static AppGameContainer app;
	
	public static final int Menu = 0;
	public static final int loginMenu = 1;
	public static final int registerMenu = 2;
	public static final int creditsMenu = 3;
	
	public mainClient(String gameName) {
		super(gameName);
		this.addState(new Menu());
		this.addState(new loginMenu());
		this.addState(new registerMenu());
	}

	public void initStatesList(GameContainer gc) throws SlickException{
		this.getState(Menu).init(gc, this);
		this.enterState(Menu);
	}

	public static void main(String[] args){
		
		try{
			app = new AppGameContainer(new mainClient("Java Eclipse Client - By Kizzaa"));
			app.setDisplayMode(800, 600, false);
			app.setTargetFrameRate(60);
			app.setShowFPS(false);
			app.start();
		}catch(SlickException e){
			e.printStackTrace();
      	}
	}
}
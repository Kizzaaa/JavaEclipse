package com.kizzaa.javaeclipse.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.BufferedImageUtil;

public class Menu extends BasicGameState {

	public static Image loginBtn;
	public static Image registerBtn;
	public static Image creditsBtn;
	public static Image exitBtn;
	public static Image background;
	public static Image news;
	
	public static String[] sNews = new String[11];
	
	public Menu(){
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		sNews = fileManage.read("data files/news.txt");
		if(!sConnection.connected){
			sConnection.connect("localhost", 54555, 54777);
		}
		
		try {
			background = loadImage("data files/graphics/gui/46.png");
			news = loadImage("data files/graphics/gui/47.png");
			loginBtn = loadImage("data files/graphics/gui/buttons/7.png");
			registerBtn = loadImage("data files/graphics/gui/buttons/8.png");
			creditsBtn = loadImage("data files/graphics/gui/buttons/9.png");
			exitBtn = loadImage("data files/graphics/gui/buttons/10.png");
		} catch (IOException e) { e.printStackTrace(); }
		
	}
	
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		drawMenu(gc, sbg, g);
		
		g.setColor(Color.white);
		for(int i=0; i<sNews.length; i++){
			if(sNews[i] != null){
				g.drawString(sNews[i], 200, 240+(20*i));
			}
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		menuClick(gc, sbg, i);
	}
	
	public static void menuClick(GameContainer gc, StateBasedGame sbg, int i) throws SlickException{
		int posX = Mouse.getX();
		int posY = 600 - Mouse.getY();
		
		if(Mouse.isButtonDown(0)){
			if(posX > 206 && posX < 295 && posY > 481 && posY < 510){
				if(sConnection.connected == false){
					JOptionPane.showMessageDialog(null, "Not connected to the server, please try again later", "Information", JOptionPane.INFORMATION_MESSAGE);
				}else{	
					sbg.enterState(mainClient.loginMenu);
				}
			}else if(posX > 306 && posX < 396 && posY > 481 && posY < 510){
				if(sConnection.connected == false){
					JOptionPane.showMessageDialog(null, "Not connected to the server, please try again later", "Information", JOptionPane.INFORMATION_MESSAGE);
				}else{
					sbg.enterState(mainClient.registerMenu);
				}
			}else if(posX > 406 && posX < 496 && posY > 481 && posY < 510){
				//credits
			}else if(posX > 506 && posX < 596 && posY > 481 && posY < 510){
				gc.exit();
			}
			
			if(sbg.getCurrentStateID() == mainClient.loginMenu){
				try{
				loginBtn = loadImage("data files/graphics/gui/buttons/7_c.png");
				registerBtn = loadImage("data files/graphics/gui/buttons/8.png");
				creditsBtn = loadImage("data files/graphics/gui/buttons/9.png");
				}catch(IOException e){ e.printStackTrace(); }
			}else if(sbg.getCurrentStateID() == mainClient.registerMenu){
				try{
				loginBtn = loadImage("data files/graphics/gui/buttons/7.png");
				registerBtn = loadImage("data files/graphics/gui/buttons/8_c.png");
				creditsBtn = loadImage("data files/graphics/gui/buttons/9.png");
				}catch(IOException e){ e.printStackTrace(); }
			}else if(sbg.getCurrentStateID() == mainClient.creditsMenu){
				try{
				loginBtn = loadImage("data files/graphics/gui/buttons/7.png");
				registerBtn = loadImage("data files/graphics/gui/buttons/8.png");
				creditsBtn = loadImage("data files/graphics/gui/buttons/9_c.png");
				}catch(IOException e){ e.printStackTrace(); }
			}else if(sbg.getCurrentStateID() == mainClient.Menu){
				try{
				loginBtn = loadImage("data files/graphics/gui/buttons/7.png");
				registerBtn = loadImage("data files/graphics/gui/buttons/8.png");
				creditsBtn = loadImage("data files/graphics/gui/buttons/9.png");
				}catch(IOException e){ e.printStackTrace(); }
			}
		}
	}
	
	public static void drawMenu(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		background.draw();
		news.draw(152, 204);
		//89x29
		loginBtn.draw(206,481);
		registerBtn.draw(306,481);
		creditsBtn.draw(406,481);
		exitBtn.draw(506,481);
		
		if(sConnection.connected){
			g.setColor(Color.green);
			g.drawString("Online",10,50);
		}else{
			g.setColor(Color.red);
			g.drawString("Offline",10,50);
		}
	}
	
	public static Image loadImage(String path) throws IOException, SlickException
	{
	    BufferedImage bufferedImage = ImageIO.read(new File(path));
	    Texture texture = BufferedImageUtil.getTexture("", bufferedImage);
	    Image image = new Image(texture.getImageWidth(), texture.getImageHeight());
	    image.setTexture(texture); 
	    return image;
	}
	
	public int getID(){
		return 0;
	}
}

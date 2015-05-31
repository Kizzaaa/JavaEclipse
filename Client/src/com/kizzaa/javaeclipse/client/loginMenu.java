package com.kizzaa.javaeclipse.client;

import java.io.IOException;

import javax.swing.JOptionPane;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class loginMenu extends BasicGameState{
	
	TextField userFld;
	PasswordTextField passFld;
	
	public loginMenu(){
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException{
		userFld = new TextField(gc, gc.getDefaultFont(), 255, 300, 300, 25);
		userFld.setText("Username");
		userFld.setCursorPos(100);
		
		passFld = new PasswordTextField(gc, gc.getDefaultFont(), 255, 350, 300, 25);
		passFld.setText("password");
		passFld.setCursorPos(100);
		
		userFld.setAcceptingInput(true);
		passFld.setAcceptingInput(true);
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {	
		
		
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Menu.drawMenu(gc, sbg, g);
		
		g.setColor(Color.white);
		g.drawString("Login", 375, 250);
		g.drawString("Login", 415, 400);
		g.drawString("Back", 340, 400);
		
		userFld.render(gc, g);
		passFld.render(gc, g);
		
	}

	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		
		int posX = Mouse.getX();
		int posY = 600 - Mouse.getY();
		
		if(Mouse.isButtonDown(0)){
			if(posX > 340 && posX < 380 && posY > 400 && posY < 420){
				sbg.enterState(0);
				try{
					Menu.loginBtn = Menu.loadImage("data files/graphics/gui/buttons/7.png");
				} catch (IOException e) { e.printStackTrace(); }
			}
			if(posX > 415 && posX < 465 && posY > 400 && posY < 420){
				sConnection.login(userFld.getText(), passFld.getText());
				JOptionPane.showMessageDialog(null, "attempting to log in...", "Information", JOptionPane.INFORMATION_MESSAGE);
				//sbg.enterState(0);
			}
		}
		
		Menu.menuClick(gc, sbg, i);
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException{
		userFld.setAcceptingInput(false);
		passFld.setAcceptingInput(false);
	}

	public int getID() {
		return 1;
	}

}

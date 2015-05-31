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

public class registerMenu extends BasicGameState{
	
	TextField userFld;
	PasswordTextField passFld;
	PasswordTextField confPassFld;
	
	public registerMenu(){
		
	}
	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException{
		userFld = new TextField(gc, gc.getDefaultFont(), 255, 300, 300, 25);
		userFld.setText("Username");
		userFld.setCursorPos(100);
		
		passFld = new PasswordTextField(gc, gc.getDefaultFont(), 255, 340, 300, 25);
		passFld.setText("password");
		passFld.setCursorPos(100);
		
		confPassFld = new PasswordTextField(gc, gc.getDefaultFont(), 255, 380, 300, 25);
		confPassFld.setText("password");
		confPassFld.setCursorPos(100);
		
		userFld.setAcceptingInput(true);
		passFld.setAcceptingInput(true);
		confPassFld.setAcceptingInput(true);
	}

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {	
		
		
	}

	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		Menu.drawMenu(gc, sbg, g);
		
		g.setColor(Color.white);
		g.drawString("Register", 370, 250);
		g.drawString("Register", 415, 420);
		g.drawString("Back", 340, 420);
		
		userFld.render(gc, g);
		passFld.render(gc, g);
		confPassFld.render(gc, g);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
		
		if(Menu.registered){
			Menu.registered = false;
			sbg.enterState(mainClient.loginMenu);
		}
		
		int posX = Mouse.getX();
		int posY = 600 - Mouse.getY();
		
		if(Mouse.isButtonDown(0)){
			if(posX > 340 && posX < 380 && posY > 420 && posY < 440){
				sbg.enterState(0);	
				try{
					Menu.registerBtn = Menu.loadImage("data files/graphics/gui/buttons/8.png");
				} catch (IOException e) { e.printStackTrace(); }
			}
			if(posX > 415 && posX < 465 && posY > 420 && posY < 440){
				if(passFld.getText().equals(confPassFld.getText())){
					sConnection.register(userFld.getText(), passFld.getText());
					JOptionPane.showMessageDialog(null, "Registering now", "Information", JOptionPane.INFORMATION_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null, "Passwords do not match", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				//sbg.enterState(0);
			}
		}
		
		Menu.menuClick(gc, sbg, i);
	}
	
	@Override
	public void leave(GameContainer gc, StateBasedGame sbg) throws SlickException{
		userFld.setAcceptingInput(false);
		passFld.setAcceptingInput(false);
		confPassFld.setAcceptingInput(false);
	}

	public int getID() {
		return 2;
	}

}

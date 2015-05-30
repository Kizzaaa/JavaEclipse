package com.kizzaa.javaeclipse.client;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.ComponentListener;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.TextField;

public class PasswordTextField extends TextField {
    
    private static final int INITIAL_KEY_REPEAT_INTERVAL = 400;
    private static final int KEY_REPEAT_INTERVAL = 50;
    private long repeatTimer;

    protected String hiddenText = "";
    private char character = '*';
    private boolean passwordVisible = false;
    
    private int lastKey;
    private char lastChar;

    public PasswordTextField(GUIContext container, Font font, int x, int y, int width,
                int height, ComponentListener listener) {
        super(container, font, x, y, width, height, listener);
    }
    
    public PasswordTextField(GUIContext container, Font font, int x, int y, int width,
         int height) {
        super(container, font, x, y, width, height);
    }
    
    public void render(GUIContext container, Graphics g) {
        if (lastKey != -1) {
            if (input.isKeyDown(lastKey)) {
                if (repeatTimer < System.currentTimeMillis()) {
                    repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
                    keyPressed(lastKey, lastChar);
                }
            } else {
                lastKey = -1;
            }
        }
        
        //get the real text
        String val = getText();
        if (passwordVisible && val.length()!=0) {
            super.render(container, g);
        } else {
            //if we need to re-build the "hidden" string
            if (val.length() != hiddenText.length()) {
                //not the most efficient way, but fast enough for our purposes
                StringBuilder buffer = new StringBuilder(val.length());
                for (int i = 0; i < val.length(); i++) {
                    buffer.append(character);
                }
                hiddenText = buffer.toString();
            }
            setText(hiddenText);
            super.render(container, g);
            setText(val);
        }
    }

    /** The 'password' character; e.g. an asterisk '*'. */
    public void setPasswordCharacter(char c) {
        if (c!=character)
            hiddenText = "";
        character = c;
    }

    public char getPasswordCharacter() {
        return character;
    }

    /** Whether or not the password should be visible in the text field. */
    public void setPasswordVisible(boolean visible) {
        passwordVisible = visible;
    }

    public boolean isPasswordVisible() {
        return passwordVisible;
    }
    
    public void keyPressed(int key, char c) {
        if (hasFocus()) {
            if (lastKey != key) {
                    lastKey = key;
                    repeatTimer = System.currentTimeMillis() + INITIAL_KEY_REPEAT_INTERVAL;
            } else {
                    repeatTimer = System.currentTimeMillis() + KEY_REPEAT_INTERVAL;
            }
            lastChar = c;
        }
        super.keyPressed(key, c);
    }
}

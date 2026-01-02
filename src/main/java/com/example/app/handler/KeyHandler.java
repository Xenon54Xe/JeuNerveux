package com.example.app.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    // STATIC
    public final static int ERASE = 8;
    public final static int ESCAPE = 27;

    // Last key
    private boolean typedLastFrame = false;
    private int lastKeyCode = 0;

    // Keys
    public boolean upPressed, leftPressed, downPressed, rightPressed;

    public boolean xPressed;

    public boolean fPressed;
    public boolean fClicked;

    public int getLastKeyCode(){
        // Return the key typed the last frame
        if (typedLastFrame){
            return lastKeyCode;
        }
        return -1;
    }

    public void update(){
        if (fClicked){
            fClicked = false;
        }

        // Allow to return a key only if it was typed the last frame
        if(typedLastFrame){
            typedLastFrame = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        int code = keyEvent.getKeyChar();

        // Last frame
        typedLastFrame = true;
        lastKeyCode = code;
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {

        int code = keyEvent.getKeyCode();

        // Other keys
        if (code == KeyEvent.VK_Z && !upPressed){
            upPressed = true;
        }
        if (code == KeyEvent.VK_Q && !leftPressed){
            leftPressed = true;
        }
        if (code == KeyEvent.VK_S && !downPressed){
            downPressed = true;
        }
        if (code == KeyEvent.VK_D && !rightPressed){
            rightPressed = true;
        }

        if (code == KeyEvent.VK_X && !xPressed){
            xPressed = true;
        }
        if (code == KeyEvent.VK_F && ! fPressed){
            fPressed = true;
            fClicked = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

        int code = keyEvent.getKeyCode();

        if (code == KeyEvent.VK_Z){
            upPressed = false;
        }
        if (code == KeyEvent.VK_Q){
            leftPressed = false;
        }
        if (code == KeyEvent.VK_S){
            downPressed = false;
        }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }

        if (code == KeyEvent.VK_X){
            xPressed = false;
        }
        if (code == KeyEvent.VK_F){
            fPressed = false;
        }
    }
}

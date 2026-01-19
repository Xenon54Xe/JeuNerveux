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

    // PLAYER MOVEMENTS
    public boolean upPressed, leftPressed, downPressed, rightPressed;

    // SPEED
    public boolean xPressed;
    public boolean cPressed;

    // SWITCH ENTITY
    public boolean fPressed;
    public boolean fClicked;
    public boolean gPressed;
    public boolean gClicked;
    public boolean rPressed;
    public boolean rClicked;

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
        if (gClicked){
            gClicked = false;
        }
        if(rClicked){
            rClicked = false;
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
        if (code == KeyEvent.VK_C && !cPressed){
            cPressed = true;
        }
        if (code == KeyEvent.VK_F && ! fPressed){
            fPressed = true;
            fClicked = true;
        }
        if (code == KeyEvent.VK_G && ! gPressed){
            gPressed = true;
            gClicked = true;
        }
        if (code == KeyEvent.VK_R && ! rPressed){
            rPressed = true;
            rClicked = true;
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
        if (code == KeyEvent.VK_C){
            cPressed = false;
        }
        if (code == KeyEvent.VK_F){
            fPressed = false;
        }
        if (code == KeyEvent.VK_G){
            gPressed = false;
        }
        if (code == KeyEvent.VK_R){
            rPressed = false;
        }
    }
}

package net.rubicon.handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    public boolean upPressed, leftPressed, downPressed, rightPressed;

    public boolean xPressed;

    public boolean fPressed;
    public boolean fClicked;

    public void update(){
        if (fClicked){
            fClicked = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {

        int code = keyEvent.getKeyCode();

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

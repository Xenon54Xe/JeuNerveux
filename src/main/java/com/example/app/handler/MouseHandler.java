package com.example.app.handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener {
    public boolean leftClickPressed, rightClickPressed; // True while key is held down
    public boolean leftClickClicked, rightClickClicked; // True only one frame

    int BUTTON_LEFT = 1;
    int BUTTON_RIGHT = 3;

    public void update(){
        // Allow to have leftClickClicked true only one or two frames
        if (leftClickClicked){
            leftClickClicked = false;
        }
        if (rightClickClicked){
            rightClickClicked = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        int button = mouseEvent.getButton();

        if (button == BUTTON_LEFT){
            leftClickPressed = true;
            leftClickClicked = true;
        }
        if (button == BUTTON_RIGHT){
            rightClickPressed = true;
            rightClickClicked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        int button = mouseEvent.getButton();

        if (button == BUTTON_LEFT){
            leftClickPressed = false;
            leftClickClicked = false;
        }
        if (button == BUTTON_RIGHT){
            rightClickPressed = false;
            rightClickClicked = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}

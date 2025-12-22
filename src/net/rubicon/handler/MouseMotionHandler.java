package net.rubicon.handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class MouseMotionHandler implements MouseMotionListener {
    int screenX, screenY;
    boolean isDragging = false;

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public boolean isDragging() {
        return isDragging;
    }

    private void updatePosition(MouseEvent mouseEvent){
        screenX = mouseEvent.getX();
        screenY = mouseEvent.getY();
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        isDragging = true;
        updatePosition(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        isDragging = false;
        updatePosition(mouseEvent);
    }
}

package com.example.app;

import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public interface IDrawable {

    void setShow(boolean active);
    
    boolean isShow();
    
    void toggleShow();

    int getWidth();

    int getHeight();

    int getDrawScreenX();

    int getDrawScreenY();

    void draw(Graphics2D g2);

    default boolean mouseOver(MouseMotionHandler mouseMH) {
        if(!isShow()){
            return false;
        }

        return mouseMH.getScreenX() > getDrawScreenX()
                && mouseMH.getScreenX() < getDrawScreenX() + getWidth()
                && mouseMH.getScreenY() > getDrawScreenY()
                && mouseMH.getScreenY() < getDrawScreenY() + getHeight();
    }

    default int getScreenX(ITrackable tracked, int worldX){
        assert tracked != null;
        return worldX - tracked.getCameraWorldX();
    }

    default int getScreenY(ITrackable tracked, int worldY){
        assert tracked != null;
        return worldY - tracked.getCameraWorldY();
    }

    default boolean isVisible(ITrackable tracked, int worldX, int worldY, int screenWidth, int screenHeight, int margin){
        int screenX, screenY;
        screenX = getScreenX(tracked, worldX);
        screenY = getScreenY(tracked, worldY);

        return screenX >= -margin && screenX <= screenWidth + margin && screenY >= -margin && screenY <= screenHeight + margin;
    }
}

package com.example.app;

import java.awt.*;

public interface IPrintable {

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

    void draw(Graphics2D g2);
}

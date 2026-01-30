package com.example.app;

import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public interface Drawable {

    int DRAW_CENTER = 0;
    int DRAW_TOP_LEFT = 1;
    int DRAW_TOP_RIGHT = 2;
    int DRAW_BOTTOM_LEFT = 3;
    int DRAW_BOTTOM_RIGHT = 4;

    void setShow(boolean show);
    
    boolean isShow();

    int getScreenX();

    int getScreenY();

    int getWidth();

    int getHeight();

    boolean isMouseOver();

    int getDrawRule();

    void setDrawRule(int drawRule);

    void draw(Graphics2D g2);

    default int getDrawTopLeftScreenX(){
        switch (getDrawRule()) {
            case DRAW_CENTER -> {
                return getScreenX() - getWidth() / 2;
            }
            case DRAW_TOP_LEFT, DRAW_BOTTOM_LEFT -> {
                return getScreenX();
            }
            case DRAW_TOP_RIGHT, DRAW_BOTTOM_RIGHT -> {
                return getScreenX() - getWidth();
            }
            default -> {
                return -1;
            }
        }
    }

    default int getDrawTopLeftScreenY(){
        switch (getDrawRule()) {
            case DRAW_CENTER -> {
                return getScreenY() - getHeight() / 2;
            }
            case DRAW_TOP_LEFT, DRAW_TOP_RIGHT -> {
                return getScreenY();
            }
            case DRAW_BOTTOM_LEFT, DRAW_BOTTOM_RIGHT -> {
                return getScreenY() - getHeight();
            }
            default -> {
                return -1;
            }
        }
    }

    default int getDrawBottomLeftScreenX(){
        return getDrawTopLeftScreenX();
    }

    default int getDrawBottomLeftScreenY(){
        return getDrawTopLeftScreenY() + getHeight();
    }

    default int getDrawTopRightScreenX(){
        return getDrawTopLeftScreenX() + getWidth();
    }

    default int getDrawTopRightScreenY(){
        return getDrawTopLeftScreenY();
    }

    default int getDrawBottomRightScreenX(){
        return getDrawTopLeftScreenX() + getWidth();
    }

    default int getDrawBottomRightScreenY(){
        return getDrawTopLeftScreenY() + getHeight();
    }

    default int getDrawCenterScreenX(){
        return getDrawTopLeftScreenX() + getWidth() / 2;
    }

    default int getDrawCenterScreenY(){
        return getDrawTopLeftScreenY() + getHeight() / 2;
    }

    default int getDrawRuleScreenX(){
        return switch (getDrawRule()){
            case DRAW_TOP_LEFT -> getDrawTopLeftScreenX();
            case DRAW_BOTTOM_LEFT -> getDrawBottomLeftScreenX();
            case DRAW_TOP_RIGHT -> getDrawTopRightScreenX();
            case DRAW_BOTTOM_RIGHT -> getDrawBottomRightScreenX();
            case DRAW_CENTER -> getDrawCenterScreenX();
            default -> throw new IllegalStateException("Unexpected value: " + getDrawRule());
        };
    }

    default int getDrawRuleScreenY(){
        return switch (getDrawRule()){
            case DRAW_TOP_LEFT -> getDrawTopLeftScreenY();
            case DRAW_BOTTOM_LEFT -> getDrawBottomLeftScreenY();
            case DRAW_TOP_RIGHT -> getDrawTopRightScreenY();
            case DRAW_BOTTOM_RIGHT -> getDrawBottomRightScreenY();
            case DRAW_CENTER -> getDrawCenterScreenY();
            default -> throw new IllegalStateException("Unexpected value: " + getDrawRule());
        };
    }

    default int[] getDrawReferenceMultiplier(){
        return switch (getDrawRule()){
            case DRAW_TOP_LEFT, DRAW_CENTER -> new int[]{1, 1};
            case DRAW_BOTTOM_LEFT -> new int[]{1, -1};
            case DRAW_TOP_RIGHT -> new int[]{-1, 1};
            case DRAW_BOTTOM_RIGHT -> new int[]{-1, -1};
            default -> throw new IllegalStateException("Unexpected value: " + getDrawRule());
        };
    }

    default boolean isMouseOver(MouseMotionHandler mouseMH) {
        if(!isShow()){
            return false;
        }

        return mouseMH.getScreenX() > getDrawTopLeftScreenX()
                && mouseMH.getScreenX() < getDrawTopLeftScreenX() + getWidth()
                && mouseMH.getScreenY() > getDrawTopLeftScreenY()
                && mouseMH.getScreenY() < getDrawTopLeftScreenY() + getHeight();
    }

    default int getScreenX(Trackable tracked, int worldX){
        // Anchor position
        assert tracked != null;
        return worldX - tracked.getCameraWorldX();
    }

    default int getScreenY(Trackable tracked, int worldY){
        // Anchor position
        assert tracked != null;
        return worldY - tracked.getCameraWorldY();
    }

    default boolean isVisible(Trackable tracked, int worldX, int worldY, int screenWidth, int screenHeight, int margin){
        int screenX, screenY;
        screenX = getScreenX(tracked, worldX);
        screenY = getScreenY(tracked, worldY);

        return screenX >= -margin && screenX <= screenWidth + margin && screenY >= -margin && screenY <= screenHeight + margin;
    }
}

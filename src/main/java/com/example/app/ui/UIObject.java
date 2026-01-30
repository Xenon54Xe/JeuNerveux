package com.example.app.ui;

import com.example.app.Drawable;
import com.example.app.utils.Vector2D;

public abstract class UIObject implements Drawable {

    // Management
    public static int NEXT_ID = 0;
    public final int id;

    // CLASS VARIABLES
    private int screenX, screenY;
    private String name;
    
    // DRAW LOGIC
    private boolean show = true;
    private int width, height;

    // DRAW REFERENCE
    private int drawRule = DRAW_CENTER;

    public UIObject(String name, int screenX, int screenY){
        this.name = name;
        this.screenX = screenX;
        this.screenY = screenY;

        id = NEXT_ID++;
    }

    public int getNextX(int x, int maxX){
        return switch (drawRule){
            case DRAW_TOP_LEFT, DRAW_BOTTOM_LEFT -> x;
            case DRAW_TOP_RIGHT, DRAW_BOTTOM_RIGHT -> maxX - x;
            default -> throw new IllegalStateException("Unexpected value: " + drawRule);
        };
    }

    public int getNextY(int y, int maxY){
        return switch (drawRule){
            case DRAW_TOP_LEFT, DRAW_TOP_RIGHT -> y;
            case DRAW_BOTTOM_LEFT, DRAW_BOTTOM_RIGHT -> maxY - y;
            default -> throw new IllegalStateException("Unexpected value: " + drawRule);
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSize(int width, int height){
        setWidth(width);
        setHeight(height);
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getDrawRule() {
        return drawRule;
    }

    public void setDrawRule(int drawRule) {
        assert drawRule == DRAW_CENTER
                || drawRule == DRAW_TOP_LEFT
                || drawRule == DRAW_BOTTOM_LEFT
                || drawRule == DRAW_TOP_RIGHT
                | drawRule == DRAW_BOTTOM_RIGHT;
        this.drawRule = drawRule;
    }

    public int getScreenX() {
        // Return the coordinates of the top left corner
        return screenX;
    }

    public void setScreenX(int screenX) {
        // Set the coordinates of the top left corner
        this.screenX = screenX;
    }

    public int getScreenY() {
        // Return the coordinates of the top left corner
        return screenY;
    }

    public void setScreenY(int screenY) {
        // Set the coordinates of the top left corner
        this.screenY = screenY;
    }

    public void setScreenPosition(Vector2D vector2D){
        setScreenPosition((int)Math.round(vector2D.getX()), (int)Math.round(vector2D.getY()));
    }

    public void setScreenPosition(int screenX, int screenY){
        setScreenX(screenX);
        setScreenY(screenY);
    }

    @Override
    public String toString() {
        return name;
    }


    public boolean equals(UIObject obj) {
    return id == obj.id;
    }
}

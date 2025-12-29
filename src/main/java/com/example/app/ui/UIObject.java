package com.example.app.ui;

import com.example.app.IPrintable;
import com.example.app.utils.Vector2D;

public abstract class UIObject implements IUIObject, IPrintable {

    // CLASS VARIABLES
    private int screenX, screenY;
    private String name;
    
    // DRAW LOGIC
    private boolean show = true;
    private boolean drawCentered = false;
    private int width, height;

    public UIObject(String name, int screenX, int screenY){
        this.name = name;
        this.screenX = screenX;
        this.screenY = screenY;
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

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isDrawCentered() {
        return drawCentered;
    }

    public void setDrawCentered(boolean drawCentered) {
        this.drawCentered = drawCentered;
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

    public int getDrawScreenX(){
        if (drawCentered) {
            return getScreenX() - width / 2;
        }
        return getScreenX();
    }

    public int getDrawScreenY(){
        if (drawCentered) {
            return getScreenY() - height / 2;
        }
        return getScreenY();
    }

    public void setScreenPosition(Vector2D position){
        setScreenX((int)position.getX());
        setScreenY((int)position.getY());
    }

    @Override
    public String toString() {
        return name;
    }
}

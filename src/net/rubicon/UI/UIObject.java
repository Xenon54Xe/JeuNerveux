package net.rubicon.UI;

import java.awt.*;

public abstract class UIObject implements IUIObject{

    // CLASS VARIABLES
    private int screenX, screenY;
    private String name;
    private boolean show = true;

    public UIObject(String name, int screenX, int screenY){
        this.name = name;
        this.screenX = screenX;
        this.screenY = screenY;
    }

    public String getName() {
        return name;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public boolean isShow() {
        return show;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean draw(Graphics2D g2) {
        return show;
    }
}

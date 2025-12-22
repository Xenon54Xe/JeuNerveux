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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScreenX() {
        return screenX;
    }

    @Override
    public int getScreenY() {
        return screenY;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    @Override
    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    @Override
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

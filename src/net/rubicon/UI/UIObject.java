package net.rubicon.UI;

import net.rubicon.main.IPrintable;
import net.rubicon.utils.Vector2D;

public abstract class UIObject implements IUIObject, IPrintable {

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

    public void setName(String name) {
        this.name = name;
    }

    public int getScreenX() {
        return screenX;
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public void setScreenPosition(Vector2D position){
        setScreenX((int)position.getX());
        setScreenY((int)position.getY());
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return name;
    }
}

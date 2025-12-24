package net.rubicon.UI;

import net.rubicon.handler.MouseMotionHandler;

import java.awt.*;

public class UIBox extends UIObject {

    // CLASS VARIABLES
    private int width, height;
    private Color boxColor;

    // UTILS
    private final MouseMotionHandler mouseMH;

    public UIBox(MouseMotionHandler mouseMH, Color boxColor, String name, int screenX, int screenY, int width, int height){
        super(name, screenX, screenY);

        this.boxColor = boxColor;
        this.width = width;
        this.height = height;

        this.mouseMH = mouseMH;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean mouseOver() {
        if(!isShow()){
            return false;
        }

        return mouseMH.getScreenX() > getScreenX()
                && mouseMH.getScreenX() < getScreenX() + width
                && mouseMH.getScreenY() > getScreenY()
                && mouseMH.getScreenY() < getScreenY() + height;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()){
            g2.setColor(boxColor);
            g2.fillRect(getScreenX(), getScreenY(), width, height); // x, y, width, height
        }
    }
}

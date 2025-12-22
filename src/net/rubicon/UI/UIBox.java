package net.rubicon.UI;

import net.rubicon.handler.MouseMotionHandler;

import java.awt.*;

public class UIBox extends UIObject implements IUIBox{

    // CLASS VARIABLES
    private int width, height;
    private Color backGroundColor;

    // UTILS
    private final MouseMotionHandler mouseMH;

    public UIBox(MouseMotionHandler mouseMH, Color backGroundColor, String name, int screenX, int screenY, int width, int height){
        super(name, screenX, screenY);

        this.backGroundColor = backGroundColor;
        this.width = width;
        this.height = height;

        this.mouseMH = mouseMH;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setBackGroundColor(Color backGroundCcolor) {
        this.backGroundColor = backGroundCcolor;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public boolean mouseOver() {
        if(! isShow()){
            return false;
        }

        return mouseMH.getScreenX() > getScreenX()
                && mouseMH.getScreenX() < getScreenX() + width
                && mouseMH.getScreenY() > getScreenY()
                && mouseMH.getScreenY() < getScreenY() + height;
    }

    @Override
    public boolean draw(Graphics2D g2) {
        if (super.draw(g2)){
            g2.setColor(backGroundColor);
            g2.fillRect(getScreenX(), getScreenY(), width, height); // x, y, width, height
            return true;
        }
        return false;
    }
}

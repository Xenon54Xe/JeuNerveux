package com.example.app.ui;

import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public class UIBox extends UIObject {

    final MouseMotionHandler mouseMH;

    // CLASS VARIABLES
    private Color boxColor;

    public UIBox(MouseMotionHandler mouseMH, Color boxColor, String name, int screenX, int screenY, int width, int height){
        super(name, screenX, screenY);

        this.mouseMH = mouseMH;

        this.boxColor = boxColor;

        setWidth(width);
        setHeight(height);
    }

    public void setBoxColor(Color boxColor) {
        this.boxColor = boxColor;
    }

    public boolean mouseOver() {
        return super.mouseOver(mouseMH);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()){
            g2.setColor(boxColor);
            g2.fillRect(getDrawScreenX(), getDrawScreenY(), getWidth(), getHeight()); // x, y, width, height
        }
    }
}

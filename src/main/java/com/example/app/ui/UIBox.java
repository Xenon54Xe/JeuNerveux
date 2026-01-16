package com.example.app.ui;

import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public class UIBox extends UIObject {

    final MouseMotionHandler mouseMH;

    // CLASS VARIABLES
    private Color boxColor;
    private boolean roundCorner = true;

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

    public boolean isRoundCorner() {
        return roundCorner;
    }

    public void setRoundCorner(boolean roundCorner) {
        this.roundCorner = roundCorner;
    }

    public boolean mouseOver() {
        return super.mouseOver(mouseMH);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()) {
            if (boxColor != null) {
                g2.setColor(boxColor);
                if (roundCorner) {
                    g2.fillRoundRect(getDrawTopLeftScreenX(), getDrawTopLeftScreenY(), getWidth(), getHeight(), 35, 35);
                } else {
                    g2.fillRect(getDrawTopLeftScreenX(), getDrawTopLeftScreenY(), getWidth(), getHeight());
                }
            }
        }
    }
}

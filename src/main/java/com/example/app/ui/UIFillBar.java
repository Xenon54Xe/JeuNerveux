package com.example.app.ui;

import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public class UIFillBar extends UIBox {

    // CLASS VARIABLES
    private final int stepX, stepY;
    private double percentFill = 1;
    private final Color fillColor;

    public UIFillBar(MouseMotionHandler mouseMH, Color boxColor, Color fillColor, String name, int screenX, int screenY, int width, int height, int stepX, int stepY) {
        super(mouseMH, boxColor, name, screenX, screenY, width, height);

        this.fillColor = fillColor;
        this.stepX = stepX;
        this.stepY = stepY;
    }

    public double getPercentFill() {
        return percentFill;
    }

    public void setPercentFill(double percentFill) {
        assert 0 <= percentFill && percentFill <= 1;
        this.percentFill = percentFill;
    }

    private int calculateWidth(){
        int maxWidth = getWidth() - 2 * stepX;
        return (int)(maxWidth * percentFill);
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()) {
            super.draw(g2);
            g2.setColor(fillColor);
            g2.fillRect(getDrawTopLeftScreenX() + stepX, getDrawTopLeftScreenY() + stepY, calculateWidth(), getHeight() - 2 * stepY);
        }
    }
}

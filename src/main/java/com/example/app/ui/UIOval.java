package com.example.app.ui;

import java.awt.*;

public class UIOval extends UIObject{

    // CLASS VARIABLES
    private final Color color;

    public UIOval(Color color, String name, int screenX, int screenY) {
        super(name, screenX, screenY);

        this.color = color;
    }

    @Override
    public boolean isMouseOver() {
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.drawOval(getScreenX(), getScreenY(), getScreenX() + getWidth(), getScreenY() + getHeight());
    }
}

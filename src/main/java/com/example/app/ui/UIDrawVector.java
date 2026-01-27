package com.example.app.ui;

import com.example.app.utils.Vector2D;

import java.awt.*;

public class UIDrawVector extends UIObject {

    private Vector2D vector2D;
    private Color vectorColor;

    public UIDrawVector(Vector2D screenPosition, Vector2D vector2D, Color vectorColor){
        super("Vector", (int)screenPosition.getX(), (int)screenPosition.getY());

        this.vector2D = vector2D;
        this.vectorColor = vectorColor;
    }

    public void setVector2D(Vector2D vector2D) {
        this.vector2D = vector2D;
    }

    public void setVectorColor(Color vectorColor) {
        this.vectorColor = vectorColor;
    }

    @Override
    public boolean isMouseOver() {
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(vectorColor);
        g2.drawLine(getDrawTopLeftScreenX(), getDrawTopLeftScreenY(), getDrawTopLeftScreenX() + (int)vector2D.getX(), getDrawTopLeftScreenY() + (int)vector2D.getY());
    }
}

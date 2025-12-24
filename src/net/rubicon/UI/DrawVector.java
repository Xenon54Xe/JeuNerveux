package net.rubicon.UI;

import net.rubicon.main.IPrintable;
import net.rubicon.utils.Vector2D;

import java.awt.*;

public class DrawVector extends UIObject implements IPrintable {

    private final Vector2D vector2D;
    private final Color vectorColor;

    public DrawVector(Vector2D screenPosition, Vector2D vector2D, Color vectorColor){
        super("Vector", (int)screenPosition.getX(), (int)screenPosition.getY());

        this.vector2D = vector2D;
        this.vectorColor = vectorColor;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(vectorColor);
        g2.drawLine(getScreenX(), getScreenY(), getScreenX() + (int)vector2D.getX(), getScreenY() + (int)vector2D.getY());
    }

    @Override
    public boolean mouseOver() {
        return false;
    }
}

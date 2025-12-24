package net.rubicon.UI;

import net.rubicon.handler.MouseMotionHandler;

import java.awt.*;

public class UIBoxText extends UIBox implements IUIText{
    /// If a big vertical bar appear, It's because height and width are not well instanced

    // CLASS VARIABLES
    private final Color textColor;
    private String text;
    private int stepX, stepY;

    public UIBoxText(MouseMotionHandler mouseMH, Color boxColor, Color textColor, String name, String text, int screenX, int screenY, int stepX, int stepY) {
        super(mouseMH, boxColor, name, screenX, screenY, 20, 100);

        this.textColor = textColor;
        this.text = text;

        this.stepX = stepX;
        this.stepY = stepY;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if(isShow()){
            int[] dimensions = calcBoxDimensions(g2, text, stepX, stepY);
            setWidth(dimensions[0]);
            setHeight(dimensions[1]);

            g2.setColor(textColor);
            g2.drawString(text, getScreenX() + stepX, getScreenY() + getHeight() - stepY);
        }
    }
}

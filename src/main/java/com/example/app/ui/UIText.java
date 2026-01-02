package com.example.app.ui;

import java.awt.*;

public class UIText extends UIObject implements IUIText {

    // CLASS VARIABLES
    private String text;
    private final Color textColor;

    public UIText(Color textColor, String text, int screenX, int screenY){
        super(text, screenX, screenY);

        this.textColor = textColor;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean mouseOver() {
        return false;
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()) {
            int[] dimensions = calcBoxDimensions(g2, text, 0, 0);
            setWidth(dimensions[0]);
            setHeight(dimensions[1]);

            g2.setColor(textColor);
            g2.drawString(text, getDrawScreenX(), getDrawScreenY());
        }
    }
}

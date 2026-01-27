package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.Updatable;
import com.example.app.handler.KeyHandler;

import java.awt.*;

public class WritableBox extends UIBox implements IUIText, Updatable {

    final GameCanvas gc;

    // CLASS VARIABLES
    private final Color textColor;
    private final int maxCharacterNumber;
    private final int stepX, stepY;

    private String text = "";
    private boolean active;

    public WritableBox(GameCanvas gc, Color boxColor, Color textColor, String name, int screenX, int screenY, int initialWidth, int stepX, int stepY, int maxCharacterNumber) {
        super(gc.mouseMH, boxColor, name, screenX, screenY, initialWidth, 0);

        this.gc = gc;

        this.textColor = textColor;
        this.maxCharacterNumber = maxCharacterNumber;
        this.stepX = stepX;
        this.stepY = stepY;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void update(){
        if (isShow()) {
            if (gc.mouseH.leftClickClicked) {
                active = mouseOver();
            }
        }

        if (active && isShow()) {
            int lastKeyCode = gc.keyH.getLastKeyCode();

            if (lastKeyCode != -1) {
                if (!text.isEmpty() && lastKeyCode == KeyHandler.ERASE) {
                    // Erase one character
                    text = text.substring(0, text.length() - 1);
                } else if (lastKeyCode != KeyHandler.ERASE){
                    text += (char) lastKeyCode;
                }
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);

        if (isShow()) {
            String textToDraw;
            if (!text.isEmpty()) {

                g2.setColor(textColor);
                StringBuilder textToDrawB;
                if (text.length() < maxCharacterNumber) {

                    textToDrawB = new StringBuilder(text);
                    while (textToDrawB.length() < maxCharacterNumber) {
                        textToDrawB.append(" ");
                    }
                } else {
                    textToDrawB = new StringBuilder(text.substring(0, maxCharacterNumber));
                }
                textToDraw = textToDrawB.toString();

            }else {
                g2.setColor(Color.DARK_GRAY);
                textToDraw = getName();
            }


            int[] dimensions = calcBoxDimensions(g2, stepX, stepY);
            setWidth(Math.max(dimensions[0], getWidth()));
            setHeight(dimensions[1]);

            g2.drawString(textToDraw, getDrawTopLeftScreenX() + stepX, getDrawTopLeftScreenY() + getHeight() - stepY);
        }
    }
}

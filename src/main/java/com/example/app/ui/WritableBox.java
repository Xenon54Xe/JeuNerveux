package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.handler.KeyHandler;

import java.awt.*;

public class WritableBox extends UIBox implements IUIText, IClickable, IUpdatable{

    final GameCanvas gc;

    // CLASS VARIABLES
    private final Color textColor;
    private final int maxCaractereNumber;
    private final int stepX, stepY;

    private String text = "";
    private boolean active;

    public WritableBox(GameCanvas gc, Color boxColor, Color textColor, String name, int screenX, int screenY, int initialWidth, int stepX, int stepY, int maxCaractereNumber) {
        super(gc.mouseMH, boxColor, name, screenX, screenY, initialWidth, 0);

        this.gc = gc;

        this.textColor = textColor;
        this.maxCaractereNumber = maxCaractereNumber;
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
        if (active) {
            int lastKeyCode = gc.keyH.getLastKeyCode();

            if (lastKeyCode != -1) {
                if (!text.isEmpty() && lastKeyCode == KeyHandler.ERASE) {
                    // Erase one character
                    text = text.substring(0, text.length() - 1);
                } else {
                    text += (char) lastKeyCode;
                }
            }
        }
    }

    @Override
    public void isClicked() {
        if (isShow()) {
            if (gc.mouseH.leftClickClicked) {
                active = mouseOver();
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
                if (text.length() < maxCaractereNumber) {

                    textToDrawB = new StringBuilder(text);
                    while (textToDrawB.length() < maxCaractereNumber) {
                        textToDrawB.append(" ");
                    }
                } else {
                    textToDrawB = new StringBuilder(text.substring(0, maxCaractereNumber));
                }
                textToDraw = textToDrawB.toString();

            }else {
                g2.setColor(Color.DARK_GRAY);
                textToDraw = getName();
            }


            int[] dimensions = calcBoxDimensions(g2, textToDraw, stepX, stepY);
            setWidth(Math.max(dimensions[0], getWidth()));
            setHeight(dimensions[1]);

            g2.drawString(textToDraw, getDrawScreenX() + stepX, getDrawScreenY() + getHeight() - stepY);
        }
    }
}

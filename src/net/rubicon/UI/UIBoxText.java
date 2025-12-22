package net.rubicon.UI;

import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UIBoxText extends UIBox implements IUIText{

    // CLASS VARIABLES
    private final Color textColor;
    private String text;
    private int stepX, stepY;


    public UIBoxText(MouseMotionHandler mouseMH, Color backGroundColor, Color textColor, String name, String text, int screenX, int screenY, int width, int height) {
        super(mouseMH, backGroundColor, name, screenX, screenY, width, height);

        this.textColor = textColor;
        this.text = text;

        calcDimensions();
    }


    private void calcDimensions(){
        setHeight(50);
        setWidth(50);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean draw(Graphics2D g2) {
        if(super.draw(g2)){
            g2.setColor(textColor);
            g2.drawString(text, getScreenX(), getScreenY() + getHeight());
            return true;
        }
        return false;
    }
}

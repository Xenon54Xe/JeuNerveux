package net.rubicon.UI;

import java.awt.*;

public class UIText extends UIObject implements IUIText {

    // CLASS VARIABLES
    private String text = "";
    private final Color textColor;

    public UIText(Color textColor, String name, int screenX, int screenY){
        super(name, screenX, screenY);

        this.textColor = textColor;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean mouseOver() {
        return false;
    }

    @Override
    public boolean draw(Graphics2D g2) {
        if (super.draw(g2)) {
            g2.setColor(textColor);
            g2.drawString(text, getScreenX(), getScreenY());
            return true;
        }
        return false;
    }
}

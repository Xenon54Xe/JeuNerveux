package net.rubicon.UI;

import net.rubicon.event.UIClickEvent;
import net.rubicon.handler.MouseHandler;
import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UITextButton extends UIBoxText implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;

    // UTILS
    UIClickEvent uiClickEvent;
    MouseHandler mouseH;
    MouseMotionHandler mouseMH;

    public UITextButton(GameCanvas gc, Color backGroundColor, Color textColor, String payload, String text, int screenX, int screenY, int stepX, int stepY) {
        super(gc.mouseMH, backGroundColor, textColor, payload, text, screenX, screenY, stepX, stepY);

        uiClickEvent = gc.uiClickEvent;
        mouseH = gc.mouseH;
        mouseMH = gc.mouseMH;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void isClicked() {
        if (active) {
            if (mouseOver() && mouseH.leftClickClicked) {
                uiClickEvent.trigger(getName() + "-left");
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                uiClickEvent.trigger(getName() + "-right");
            }
        }
    }
}

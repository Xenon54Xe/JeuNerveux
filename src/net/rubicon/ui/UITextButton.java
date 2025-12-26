package net.rubicon.ui;

import net.rubicon.event.ComponentUIClick;
import net.rubicon.event.Event;
import net.rubicon.handler.MouseHandler;
import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UITextButton extends UIBoxText implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;

    // UTILS
    Event eventUIClick;
    MouseHandler mouseH;
    MouseMotionHandler mouseMH;

    public UITextButton(GameCanvas gc, Color backGroundColor, Color textColor, String payload, String text, int screenX, int screenY, int stepX, int stepY) {
        super(gc.mouseMH, backGroundColor, textColor, payload, text, screenX, screenY, stepX, stepY);

        eventUIClick = gc.eventUIClick;
        mouseH = gc.mouseH;
        mouseMH = gc.mouseMH;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void isClicked() {
        if (active && isShow()) {
            if (mouseOver() && mouseH.leftClickClicked) {
                eventUIClick.trigger(new ComponentUIClick(this, ComponentUIClick.LEFT_BUTTON));
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                eventUIClick.trigger(new ComponentUIClick(this, ComponentUIClick.RIGHT_BUTTON));
            }
        }
    }
}

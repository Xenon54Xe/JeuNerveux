package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentUIClick;
import com.example.app.event.Event;
import com.example.app.handler.MouseHandler;
import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public class UITextButton extends UIBoxText implements IClickable{

    final Event eventUIClick;
    final MouseHandler mouseH;
    final MouseMotionHandler mouseMH;

    // CLASS VARIABLES
    private boolean active = true;

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

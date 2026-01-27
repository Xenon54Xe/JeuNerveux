package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.Updatable;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.IEvent;
import com.example.app.handler.MouseHandler;
import com.example.app.handler.MouseMotionHandler;

import java.awt.*;

public class UITextButton extends UIBoxText implements Updatable {

    final IEvent eventUIClick;
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
    public void update() {
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

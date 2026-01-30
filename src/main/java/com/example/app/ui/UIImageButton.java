package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.Updatable;
import com.example.app.event.IEvent;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.handler.MouseHandler;

import java.awt.*;

public class UIImageButton extends UIBoxImage implements Updatable {

    final IEvent eventUIClick;
    final MouseHandler mouseH;

    // CLASS VARIABLES
    private boolean active = true;

    public UIImageButton(GameCanvas gc, Image image, Color backGroundColor, String payload, int screenX, int screenY, int width, int height, int imageWidth, int imageHeight) {
        super(gc.mouseMH, image, backGroundColor, payload, screenX, screenY, width, height, imageWidth, imageHeight);

        mouseH = gc.mouseH;
        eventUIClick = gc.eventUIClick;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update() {
        if (isActive() && isShow()) {
            if (mouseOver() && mouseH.leftClickClicked) {
                eventUIClick.trigger(new ComponentUIClick(this, ComponentUIClick.LEFT_BUTTON));
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                eventUIClick.trigger(new ComponentUIClick(this, ComponentUIClick.RIGHT_BUTTON));
            }
        }
    }
}

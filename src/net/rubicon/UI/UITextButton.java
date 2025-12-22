package net.rubicon.UI;

import net.rubicon.event.UIClickEvent;
import net.rubicon.handler.MouseHandler;
import net.rubicon.handler.MouseMotionHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UITextButton extends UIBoxText implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;
    private String payload;

    // UTILS
    UIClickEvent uiClickEvent;
    MouseHandler mouseH;
    MouseMotionHandler mouseMH;

    public UITextButton(GameCanvas gc, Color backGroundColor, Color textColor, String name, String text, String payload, int screenX, int screenY, int width, int height) {
        super(gc.mouseMH, backGroundColor, textColor, name, text, screenX, screenY, width, height);

        uiClickEvent = gc.uiClickEvent;
        mouseH = gc.mouseH;
        mouseMH = gc.mouseMH;

        this.payload = payload;
    }


    @Override
    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String getPayload() {
        return payload;
    }

    @Override
    public void isClicked() {
        if (active) {
            if (mouseOver() && mouseH.leftClickClicked) {
                uiClickEvent.trigger(payload + "-left");
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                uiClickEvent.trigger(payload + "-right");
            }
        }
    }
}

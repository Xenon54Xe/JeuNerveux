package net.rubicon.UI;

import net.rubicon.event.UIClickEvent;
import net.rubicon.handler.MouseHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UIImageButton extends UIBoxImage implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;
    private String payload;

    // UTILS
    private final UIClickEvent uiClickEvent;
    private final MouseHandler mouseH;

    public UIImageButton(GameCanvas gc, Image image, Color backGroundColor, String name, String payload, int screenX, int screenY, int width, int height, int imageWidth, int imageHeight) {
        super(gc.mouseMH, image, backGroundColor, name, screenX, screenY, width, height, imageWidth, imageHeight);

        this.payload = payload;
        mouseH = gc.mouseH;
        uiClickEvent = gc.uiClickEvent;
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

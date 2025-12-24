package net.rubicon.UI;

import net.rubicon.event.UIClickEvent;
import net.rubicon.event.UIClickEventComponent;
import net.rubicon.handler.MouseHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UIImageButton extends UIBoxImage implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;

    // UTILS
    private final UIClickEvent uiClickEvent;
    private final MouseHandler mouseH;

    public UIImageButton(GameCanvas gc, Image image, Color backGroundColor, String payload, int screenX, int screenY, int width, int height, int imageWidth, int imageHeight) {
        super(gc.mouseMH, image, backGroundColor, payload, screenX, screenY, width, height, imageWidth, imageHeight);

        mouseH = gc.mouseH;
        uiClickEvent = gc.uiClickEvent;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void isClicked() {
        if (active) {
            if (mouseOver() && mouseH.leftClickClicked) {
                uiClickEvent.trigger(new UIClickEventComponent(this, UIClickEventComponent.LEFT_BUTTON));
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                uiClickEvent.trigger(new UIClickEventComponent(this, UIClickEventComponent.RIGHT_BUTTON));
            }
        }
    }
}

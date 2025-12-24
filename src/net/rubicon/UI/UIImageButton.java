package net.rubicon.UI;

import net.rubicon.event.ECUIClick;
import net.rubicon.event.Event;
import net.rubicon.handler.MouseHandler;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class UIImageButton extends UIBoxImage implements IClickable{

    // CLASS VARIABLES
    private boolean active = true;

    // UTILS
    private final Event eventUIClick;
    private final MouseHandler mouseH;

    public UIImageButton(GameCanvas gc, Image image, Color backGroundColor, String payload, int screenX, int screenY, int width, int height, int imageWidth, int imageHeight) {
        super(gc.mouseMH, image, backGroundColor, payload, screenX, screenY, width, height, imageWidth, imageHeight);

        mouseH = gc.mouseH;
        eventUIClick = gc.eventUIClick;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void isClicked() {
        if (active) {
            if (mouseOver() && mouseH.leftClickClicked) {
                eventUIClick.trigger(new ECUIClick(this, ECUIClick.LEFT_BUTTON));
            }
            if (mouseOver() && mouseH.rightClickClicked) {
                eventUIClick.trigger(new ECUIClick(this, ECUIClick.RIGHT_BUTTON));
            }
        }
    }
}

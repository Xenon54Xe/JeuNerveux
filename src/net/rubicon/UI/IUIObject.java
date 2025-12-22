package net.rubicon.UI;

import java.awt.*;

public interface IUIObject {

    public String getName();

    public int getScreenX();

    public int getScreenY();

    public void setName(String name);

    public void setScreenX(int screenX);

    public void setScreenY(int screenY);

    public void setShow(boolean show);

    public boolean mouseOver();

    public boolean draw(Graphics2D g2);
}

package net.rubicon.UI;

import java.awt.*;

public interface IUIText{

    default int[] calcBoxDimensions(Graphics2D g2, String text, int stepX, int stepY){
        /// Return width and height
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(text) + 2 * stepX;
        int height = fm.getHeight() / 2 + 2 * stepY;

        return new int[]{width, height};
    }
}

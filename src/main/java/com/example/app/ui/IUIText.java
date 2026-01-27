package com.example.app.ui;

import java.awt.*;

public interface IUIText{

    void setText(String text);

    String getText();

    default int[] calcBoxDimensions(Graphics2D g2, int stepX, int stepY){
        /// Return width and height
        FontMetrics fm = g2.getFontMetrics();
        int width = fm.stringWidth(getText()) + 2 * stepX;
        int height = fm.getHeight() / 2 + 2 * stepY;

        return new int[]{width, height};
    }
}

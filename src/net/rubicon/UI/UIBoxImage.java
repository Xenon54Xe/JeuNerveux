package net.rubicon.UI;


import net.rubicon.handler.MouseMotionHandler;

import java.awt.*;

public class UIBoxImage extends UIBox{

    // CLASS VARIABLES
    private Image image;
    private int imageWidth, imageHeight;
    private int stepX, stepY;

    public UIBoxImage(MouseMotionHandler mouseMH, Image image, Color backGroundColor, String name, int screenX, int screenY, int width, int height, int imageWidth, int imageHeight) {
        super(mouseMH, backGroundColor, name, screenX, screenY, width, height);

        this.image = image;
        setImageWidth(imageWidth);
        setImageHeight(imageHeight);
    }


    public void setImage(Image image) {
        this.image = image;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
        stepX = (getWidth() - imageWidth) / 2;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
        stepY = (getHeight() - imageHeight) / 2;
    }

    @Override
    public boolean draw(Graphics2D g2) {
        if (super.draw(g2)){
            g2.drawImage(image, getScreenX() + stepX, getScreenY() + stepY, imageWidth, imageHeight, null);
            return true;
        }
        return false;
    }
}

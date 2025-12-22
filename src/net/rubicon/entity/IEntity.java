package net.rubicon.entity;

import java.awt.image.BufferedImage;

public interface IEntity {

    public BufferedImage getSprite(String spriteName);

    public int getSpriteNum();
    public double getWorldX();
    public double getWorldY();
    public int getSpeed();
    public String getDirection();

    public void setSprite(String spriteName, BufferedImage spriteImage);
    public void setWorldX(double worldX);
    public void setWorldY(double worldY);
    public void setSpeed(int speed);
    public void setDirection(String direction);

    public void addWorldX(double step);
    public void addWorldY(double step);

    public void updateShowedSprite(); // To animate the sprite using up1 and up2 for example
}

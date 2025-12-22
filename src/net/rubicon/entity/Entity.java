package net.rubicon.entity;

import java.awt.image.BufferedImage;

public abstract class Entity implements IEntity{

    // IMAGES
    private BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    // IMAGES LOGIC
    private int spriteCounter = 0;
    private int spriteNum = 1;

    // DATA
    private double worldX, worldY;
    private int speed;
    private String direction;

    public Entity(){

    }

    public Entity(BufferedImage up1, BufferedImage up2, BufferedImage down1, BufferedImage down2,
                  BufferedImage left1, BufferedImage left2, BufferedImage right1, BufferedImage right2){
        this.up1 = up1;
        this.up2 = up2;
        this.down1 = down1;
        this.down2 = down2;
        this.left1 = left1;
        this.left2 = left2;
        this.right1 = right1;
        this.right2 = right2;

    }

    @Override
    public BufferedImage getSprite(String spriteName) {
        return switch (spriteName) {
            case "up1" -> up1;
            case "up2" -> up2;
            case "down1" -> down1;
            case "down2" -> down2;
            case "left1" -> left1;
            case "left2" -> left2;
            case "right1" -> right1;
            case "right2" -> right2;
            default -> null;
        };
    }

    @Override
    public int getSpriteNum() {
        return spriteNum;
    }

    @Override
    public double getWorldX() {
        return worldX;
    }

    @Override
    public double getWorldY() {
        return worldY;
    }

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    public String getDirection() {
        return direction;
    }

    @Override
    public void setSprite(String spriteName, BufferedImage spriteImage) {
        switch (spriteName) {
            case "up1":
                up1 = spriteImage;
                break;
            case "up2":
                up2 = spriteImage;
                break;
            case "down1":
                down1 = spriteImage;
                break;
            case "down2":
                down2 = spriteImage;
                break;
            case "left1":
                left1 = spriteImage;
                break;
            case "left2":
                left2 = spriteImage;
                break;
            case "right1":
                right1 = spriteImage;
                break;
            case "right2":
                right2 = spriteImage;
                break;
        }
    }

    @Override
    public void setWorldX(double worldX) {
        this.worldX = worldX;
    }

    @Override
    public void setWorldY(double worldY) {
        this.worldY = worldY;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public void updateShowedSprite() {
        spriteCounter++;
        if (spriteCounter > 6){
            if (spriteNum == 1){
                spriteNum = 2;
            }
            else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
    }

    @Override
    public void addWorldX(double step) {
        worldX += step;
    }

    @Override
    public void addWorldY(double step) {
        worldY += step;
    }
}

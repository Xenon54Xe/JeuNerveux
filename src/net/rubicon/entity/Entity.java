package net.rubicon.entity;

import net.rubicon.main.GameCanvas;
import net.rubicon.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public abstract class Entity implements IEntity{

    // UTILS
    GameCanvas gc;

    // IMAGES
    private BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    // IMAGES LOGIC
    private int spriteCounter = 0;
    private int spriteNum = 1;

    // DATA
    Vector2D worldPosition = Vector2D.ZERO;
    Vector2D moveVector = Vector2D.DOWN; // Must be normalized before used in movement
    private String drawDirection;
    private int speed;

    // COLLISION
    public Rectangle solidArea;

    public Entity(GameCanvas gc){
        this.gc = gc;
    }


    // SPRITES
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

    public int getSpriteNum() {
        return spriteNum;
    }

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


    // DRAW DIRECTION
    public String getDrawDirection() {
        return drawDirection;
    }

    public void setDrawDirection(String drawDirection) {
        this.drawDirection = drawDirection;
    }

    public void updateDrawDirection(){
        String direction = moveVector.getMainDirection();
        if (!Objects.equals(direction, "None")){
            drawDirection = direction;
        }
    }


    // POSITION / SPEED
    public Vector2D getWorldPosition(){
        return worldPosition.copy();
    }

    public void setWorldPosition(Vector2D worldPosition){
        this.worldPosition = worldPosition;
    }

    public double getWorldX() {
        return worldPosition.getX();
    }

    public double getWorldY() {
        return worldPosition.getY();
    }

    public int getTileX(){
        return (int)(getWorldX() / gc.tileSize + 0.5);
    }

    public int getTileY(){
        return (int)(getWorldY() / gc.tileSize + 1);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // MOVE
    public Vector2D getMoveVector(){
        return moveVector.copy();
    }

    public void setMoveVector(Vector2D moveVector) {
        this.moveVector = moveVector;
    }

    public void addMoveVector(Vector2D moveVector){
        this.moveVector = this.moveVector.add(moveVector);
    }

    public void move(Vector2D vector2D) {
        worldPosition = worldPosition.add(vector2D);
    }
}

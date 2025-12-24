package net.rubicon.entity;

import net.rubicon.main.GameCanvas;
import net.rubicon.utils.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Entity implements IEntity, ITrackable{

    // UTILS
    final GameCanvas gc;

    // CLASS VARIABLES
    private final String name;

    // IMAGES
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    // IMAGES LOGIC
    private int spriteCounter = 0;
    private int spriteNum = 1;

    // DATA
    Vector2D worldPosition = Vector2D.ZERO;
    Vector2D moveVector = Vector2D.DOWN; // Must be normalized before used in movement
    private int speed;

    // SHOW
    private String drawDirection = "down";
    private boolean show = true;

    // COLLISION
    private Rectangle solidArea;

    // CENTER OF ENTITY
    private final int width, height;

    public Entity(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height){
        this.gc = gc;
        this.solidArea = solidArea;
        this.speed = speed;
        this.name = name;

        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
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

    public BufferedImage getSpriteImage(String filePath){
        try{
            return ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(filePath)));
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
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

    public boolean isShow() {
        return show;
    }

    // POSITION / SPEED
    public Vector2D getWorldPosition() {
        return worldPosition;
    }

    public void setWorldPosition(Vector2D worldPosition) {
        this.worldPosition = worldPosition;
    }

    public double getWorldX() {
        return worldPosition.getX();
    }

    public double getWorldY() {
        return worldPosition.getY();
    }

    public int getScreenX(){
        return (int)ITrackable.super.calcTrackedEntityScreenX(gc.screenWidth, gc.worldWidth);
    }

    public int getScreenY(){
        return (int)ITrackable.super.calcTrackedEntityScreenY(gc.screenHeight, gc.worldHeight);
    }

    public int getCameraWorldX(){
        return (int)ITrackable.super.calcCameraWorldX(gc.screenWidth, gc.worldWidth);
    }

    public int getCameraWorldY(){
        return (int)ITrackable.super.calcCameraWorldY(gc.screenHeight, gc.worldHeight);
    }

    public int getTileX(){
        return (int)(getWorldX() / gc.tileSize);
    }

    public int getTileY(){
        return (int)(getWorldY() / gc.tileSize + 0.5);
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }
}

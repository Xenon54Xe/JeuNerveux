package net.rubicon.entity;

import net.rubicon.main.GameCanvas;
import net.rubicon.main.IPrintable;
import net.rubicon.main.ITrackable;
import net.rubicon.utils.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Entity implements IEntity, IPrintable, ITrackable {

    // UTILS
    final GameCanvas gc;

    // CLASS VARIABLES
    private final String name;
    private boolean active = true;

    // IMAGES
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    // IMAGES LOGIC
    private int spriteCounter = 0;
    private int spriteNum = 1;

    // DATA
    Vector2D worldPosition = Vector2D.ZERO;
    Vector2D moveDirectionVector = Vector2D.DOWN; // Must be normalized before used in movement
    private int speed;

    // SHOW
    private String drawDirection = Vector2D.S_DOWN;
    private boolean show = true;

    // COLLISION
    private Rectangle solidArea;
    private boolean avoidWall;

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
        String direction = moveDirectionVector.getMainDirection();
        if (!direction.equals(Vector2D.S_ZERO)){
            drawDirection = direction;
        }
    }

    public boolean isShow() {
        return show;
    }

    // POSITION / SPEED
    public Vector2D getWorldPosition() {
        return worldPosition.copy();
    }

    public void setWorldPosition(Vector2D worldPosition) {
        this.worldPosition = worldPosition;
    }

    public void setRandomPosition(){
        int randomIndex = (int)Math.floor(Math.random() * gc.tileM.spawnableTiles.size());
        int randomNumber = gc.tileM.spawnableTiles.get(randomIndex);

        int col, row;
        col = randomNumber % gc.maxWorldCol;
        row = randomNumber / gc.maxWorldRow;

        setTilePosition(col, row);
    }

    public double getWorldX() {
        // The world pos of the drawn entity's center
        return worldPosition.getX();
    }

    public double getWorldY() {
        // The world pos of the drawn entity's center
        return worldPosition.getY();
    }

    public int getScreenX() {
        return IPrintable.super.getScreenX(gc.tracked, (int)getWorldX());
    }

    public int getScreenY() {
        return IPrintable.super.getScreenY(gc.tracked, (int)getWorldY());
    }

    public Vector2D getScreenPosition(){
        return new Vector2D(getScreenX(), getScreenY());
    }

    public int getCameraWorldX(){
        return (int)ITrackable.super.calcCameraWorldX(gc.screenWidth, gc.worldWidth);
    }

    public int getCameraWorldY(){
        return (int)ITrackable.super.calcCameraWorldY(gc.screenHeight, gc.worldHeight);
    }

    public int getTileX(){
        // Position du centre
        return Vector2D.getTileX(gc.tileSize, getWorldX());
    }

    public int getTileY(){
        // Position des pieds
        return Vector2D.getTileY(gc.tileSize, getWorldY() + gc.tileSize / 2.0);
    }

    public void setTilePosition(int col, int row){
        setWorldPosition(new Vector2D(gc.tileSize * col + getWidth() / 2.0, gc.tileSize * row + getHeight() / 2.0));
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
    public Vector2D getMoveDirectionVector(){
        return moveDirectionVector;
    }

    public void setMoveDirectionVector(Vector2D moveDirectionVector) {
        assert Math.abs(moveDirectionVector.getLength() - 1) < 0.00001;
        this.moveDirectionVector = moveDirectionVector;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvoidWall() {
        return avoidWall;
    }

    public void setAvoidWall(boolean avoidWall) {
        this.avoidWall = avoidWall;
    }

    public void drawWalkingAnimation(Graphics2D g2){
        BufferedImage image = null;

        // CHOOSE THE NEXT IMAGE
        String drawDirection = getDrawDirection();
        if (drawDirection.equals(Vector2D.S_UP)){
            if (getSpriteNum() == 1) {
                image = getSprite("up1");
            }
            if (getSpriteNum() == 2) {
                image = getSprite("up2");
            }
        }else if (drawDirection.equals(Vector2D.S_DOWN)){
            if (getSpriteNum() == 1) {
                image = getSprite("down1");
            }
            if (getSpriteNum() == 2) {
                image = getSprite("down2");
            }
        } else if (drawDirection.equals(Vector2D.S_LEFT)) {
            if (getSpriteNum() == 1) {
                image = getSprite("left1");
            }
            if (getSpriteNum() == 2) {
                image = getSprite("left2");
            }
        } else if (drawDirection.equals(Vector2D.S_RIGHT)) {
            if (getSpriteNum() == 1) {
                image = getSprite("right1");
            }
            if (getSpriteNum() == 2) {
                image = getSprite("right2");
            }
        }

        g2.drawImage(image, getScreenX() - getWidth() / 2, getScreenY() - getHeight() / 2, width, height, null);
    }

    public void move(Vector2D vector2D) {
        // Make the entity move using its moveVectorDirection
        worldPosition = worldPosition.add(vector2D);
    }

    public void move(double dt){
        move(moveDirectionVector.mul(getSpeed() * dt));
    }
}

package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.Trackable;
import com.example.app.Drawable;
import com.example.app.Updatable;
import com.example.app.utils.FileUtils;
import com.example.app.utils.ILoopList;
import com.example.app.utils.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Entity implements Updatable, Drawable, Trackable, IEntity {

    // UTILS
    public final GameCanvas gc;

    // STATIC
    public static int NEXT_ID = 0;

    // IMAGES
    public BufferedImage left1, left2, right1, right2;
    // IMAGES LOGIC
    private int drawDirection = Vector2D.S_LEFT;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private final int waitTimeBeforeAnimation;

    // CLASS VARIABLES
    private Rectangle solidArea;
    private final String name;
    private int speed;
    private final int width, height;
    private final int id;

    // MOVEMENT
    private Vector2D worldPosition = Vector2D.ZERO;
    private Vector2D moveDirectionVector = Vector2D.DOWN; // Must be normalized before used in movement

    // STATUS
    private boolean show = true;
    private boolean active = true;
    private boolean avoidWall;
    private boolean ownBehavior = true;

    public Entity(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int waitTimeBeforeAnimation){
        this.gc = gc;

        this.solidArea = solidArea;
        this.speed = speed;
        this.name = name;
        this.width = width;
        this.height = height;

        this.waitTimeBeforeAnimation = waitTimeBeforeAnimation;
        spriteCounter = (int) (Math.random() * waitTimeBeforeAnimation);

        // ID
        id = NEXT_ID++;
    }

    public String getName() {
        return name;
    }

    // SPRITES
    public BufferedImage getSprite(String spriteName) {
        return switch (spriteName) {
            case "left1"-> left1;
            case "left2" -> left2;
            case "right1" -> right1;
            case "right2" -> right2;
            default -> throw new IllegalStateException("Unexpected value: " + spriteName);
        };
    }

    public BufferedImage getSpriteImage(String directory, String fileName){
        try{
            return ImageIO.read(Objects.requireNonNull(FileUtils.loadFile(directory, fileName)));
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
        if (spriteCounter > waitTimeBeforeAnimation){
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
    public int getDrawDirection() {
        return drawDirection;
    }

    public void setDrawDirection(int drawDirection) {
        this.drawDirection = drawDirection;
    }

    public void updateDrawDirection(){
        ArrayList<Integer> directions = moveDirectionVector.getDirections();
        if (directions == null){
            return;
        }

        for (int direction : directions){
            if (direction == Vector2D.S_LEFT){
                setDrawDirection(Vector2D.S_LEFT);
            } else if (direction == Vector2D.S_RIGHT) {
                setDrawDirection(Vector2D.S_RIGHT);
            }
        }
    }

    public boolean isShow() {
        return show;
    }

    @Override
    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isVisible(){
        return Drawable.super.isVisible(gc.tracked, (int)getWorldX(), (int)getWorldY(), gc.SCREEN_WIDTH, gc.SCREEN_HEIGHT, gc.TILE_SIZE);
    }

    // POSITION / SPEED
    public Vector2D getWorldPosition() {
        return worldPosition;
    }

    public Vector2D getWorldTopLeftPosition(){
        return getWorldPosition().sub(new Vector2D(width / 2.0, height / 2.0));
    }

    public void setWorldPosition(Vector2D worldPosition) {
        this.worldPosition = worldPosition;
    }

    public void setRandomTilePosition(ILoopList<Integer> choiceTiles){
        setTilePosition(Vector2D.chooseRandomTile(gc, choiceTiles));
    }

    public double getWorldX() {
        // The world pos of the drawn entity's center
        return worldPosition.getX();
    }

    public double getWorldY() {
        // The world pos of the drawn entity's center
        return worldPosition.getY();
    }

    public double getTopLeftWorldX(){
        return getWorldX() - width / 2.0;
    }

    public double getTopLeftWorldY(){
        return getWorldY() - height / 2.0;
    }

    public int getScreenX() {
        return Drawable.super.getScreenX(gc.tracked, (int)getWorldX());
    }

    public int getScreenY() {
        return Drawable.super.getScreenY(gc.tracked, (int)getWorldY());
    }

    public Vector2D getScreenPosition(){
        return new Vector2D(getScreenX(), getScreenY());
    }

    public int getCameraWorldX(){
        return (int) Trackable.super.calcCameraWorldX(gc.SCREEN_WIDTH, gc.tileM.getWorldWidth());
    }

    public int getCameraWorldY(){
        return (int) Trackable.super.calcCameraWorldY(gc.SCREEN_HEIGHT, gc.tileM.getWorldHeight());
    }

    public int getTileX(){
        // Position du centre
        return Vector2D.getTileX(gc.TILE_SIZE, getWorldX());
    }

    public int getTileY(){
        // Position des pieds
        return Vector2D.getTileY(gc.TILE_SIZE, getWorldY() + gc.TILE_SIZE / 2.0);
    }

    public void setTilePosition(int col, int row){
        setWorldPosition(new Vector2D(gc.TILE_SIZE * col + getWidth() / 2.0, gc.TILE_SIZE * row + getHeight() / 2.0));
    }

    public void setTilePosition(int[] position){
        setTilePosition(position[0], position[1]);
    }

    public void setTilePosition(Vector2D position){
        setTilePosition((int)position.getX(), (int)position.getY());
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

    public Vector2D getNextMoveVector(double dt){
        return moveDirectionVector.mul(getSpeed() * dt);
    }

    public void setMoveDirectionVector(Vector2D moveDirectionVector) {
        assert moveDirectionVector.isNormalized();
        this.moveDirectionVector = moveDirectionVector;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean mouseOver() {
        return Drawable.super.isMouseOver(gc.mouseMH);
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

    public boolean isOwnBehavior() {
        return ownBehavior;
    }

    public void setOwnBehavior(boolean ownBehavior) {
        this.ownBehavior = ownBehavior;
    }

    public int getWaitTimeBeforeAnimation() {
        return waitTimeBeforeAnimation;
    }

    public void drawWalkingAnimation(Graphics2D g2){

        if (isVisible()) {
            BufferedImage image = null;

            // CHOOSE THE NEXT IMAGE
            int drawDirection = getDrawDirection();
            switch (drawDirection) {
                case Vector2D.S_UP -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("up1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("up2");
                    }
                }
                case Vector2D.S_DOWN -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("down1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("down2");
                    }
                }
                case Vector2D.S_LEFT -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("left1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("left2");
                    }
                }
                case Vector2D.S_RIGHT -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("right1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("right2");
                    }
                }
            }

            assert image != null;
            g2.drawImage(image, getDrawTopLeftScreenX(), getDrawTopLeftScreenY(), width, height, null);
        }
    }

    private void move(Vector2D vector2D) {
        // Make the entity move using its moveVectorDirection
        worldPosition = worldPosition.add(vector2D);
    }

    public void move(double dt) {
        // Anticipate collisions
        Vector2D correctedMoveVector = gc.cChecker.checkTile(this, getNextMoveVector(dt)); // Disable moving if collisions will happen

        move(correctedMoveVector);
    }

    public void move(ArrayList<Entity> entities, double dt){
        // Anticipate collisions with entities
        Vector2D correctedMoveVector = gc.cChecker.checkTile(this, getNextMoveVector(dt)); // Disable moving if collisions will happen
        correctedMoveVector = gc.cChecker.checkEntity(entities, this, correctedMoveVector);

        move(correctedMoveVector);
    }

    public boolean equals(Entity other) {
        assert other != null;
        return id == other.id;
    }

    @Override
    public int getDrawRule() {
        return DRAW_CENTER;
    }

    @Override
    public void setDrawRule(int drawRule) {

    }

    @Override
    public boolean isMouseOver() {
        return isMouseOver(gc.mouseMH);
    }

    @Override
    public String toString() {
        return name;
    }
}

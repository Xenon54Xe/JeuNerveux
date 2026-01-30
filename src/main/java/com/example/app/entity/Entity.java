package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.Trackable;
import com.example.app.Drawable;
import com.example.app.Updatable;
import com.example.app.entity.group.EntityGroup;
import com.example.app.utils.*;
import com.example.app.utils.collections.List;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public abstract class Entity implements Updatable, Drawable, Trackable, IEntity {

    // UTILS
    public final GameCanvas gc;

    // STATIC
    public static int NEXT_ID = 0;

    // IMAGES
    public BufferedImage left1, left2, right1, right2;
    // IMAGES LOGIC
    private int drawDirection = Vector2D.INT_LEFT;
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private final int waitTimeBeforeAnimation;

    // CLASS VARIABLES
    private final Rectangle solidArea;
    private final String name;
    private int speed;
    private final int width, height;
    private final int id;

    // MOVEMENT
    private final Vector2D worldPosition = Vector2D.ZERO;
    private final Vector2D moveDirectionVector = Vector2D.DOWN; // Must be normalized before used in movement

    // STATUS
    private boolean show = true;
    private boolean active = true;
    private boolean avoidWall;
    private boolean moveSpontaneously = true;

    // GROUP
    private int groupID = EntityGroup.NULL_GROUP_ID;

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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
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
        int[] directions = moveDirectionVector.getDirections();
        if (directions == null){
            return;
        }

        for (int i = 0; i < directions.length; i++) {
            int direction = directions[i];

            if (direction == Vector2D.INT_LEFT){
                setDrawDirection(Vector2D.INT_LEFT);
            } else if (direction == Vector2D.INT_RIGHT) {
                setDrawDirection(Vector2D.INT_RIGHT);
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

    @Deprecated
    public Vector2D getWorldTopLeftPosition(){
        Vector2D vector2D = getWorldPosition().copy();
        vector2D.sub(width / 2.0, height / 2.0);
        return vector2D;
    }

    public void setWorldPosition(Vector2D position) {
        worldPosition.setX(position.getX());
        worldPosition.setY(position.getY());
    }

    public void setWorldPosition(double x, double y) {
        worldPosition.setX(x);
        worldPosition.setY(y);
    }

    public void setRandomTilePosition(List<Integer> choiceTiles){
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

    public double getLeft(){
        return getWorldX() - width / 2.0;
    }

    public double getTop(){
        return getWorldY() - height / 2.0;
    }

    public double getRight(){
        return getWorldX() + width / 2.0;
    }

    public double getBottom(){
        return getWorldY() + height / 2.0;
    }

    public int getScreenX() {
        return Drawable.super.getScreenX(gc.tracked, (int)getWorldX());
    }

    public int getScreenY() {
        return Drawable.super.getScreenY(gc.tracked, (int)getWorldY());
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
        setWorldPosition(gc.TILE_SIZE * col + getWidth() / 2.0, gc.TILE_SIZE * row + getHeight() / 2.0);
    }

    public void setTilePosition(int[] position){
        setWorldPosition(position[0], position[1]);
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
        Vector2D vector2D = moveDirectionVector.copy();
        vector2D.mul(getSpeed() * dt);
        return vector2D;
    }

    public void setMoveDirectionVector(Vector2D vector){
        moveDirectionVector.setX(vector.getX());
        moveDirectionVector.setY(vector.getY());
        assert moveDirectionVector.isNormalized();
    }

    public void setMoveDirectionVector(double x, double y){
        moveDirectionVector.setX(x);
        moveDirectionVector.setY(y);
        assert moveDirectionVector.isNormalized();
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(int x, int y, int width, int height) {
        solidArea.x = x;
        solidArea.y = y;
        solidArea.width = width;
        solidArea.height = height;
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

    public boolean isMoveSpontaneously() {
        return moveSpontaneously;
    }

    public void setMoveSpontaneously(boolean moveSpontaneously) {
        this.moveSpontaneously = moveSpontaneously;
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
                case Vector2D.INT_UP -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("up1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("up2");
                    }
                }
                case Vector2D.INT_DOWN -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("down1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("down2");
                    }
                }
                case Vector2D.INT_LEFT -> {
                    if (getSpriteNum() == 1) {
                        image = getSprite("left1");
                    }
                    if (getSpriteNum() == 2) {
                        image = getSprite("left2");
                    }
                }
                case Vector2D.INT_RIGHT -> {
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
        worldPosition.add(vector2D);
    }

    public void move(double dt) {
        // Anticipate collisions
        Vector2D moveVector = getNextMoveVector(dt);
        gc.cChecker.checkTile(this, moveVector); // Disable moving if collisions will happen

        move(moveVector);
    }

    public void move(List<Entity> entities, double dt){
        // Anticipate collisions with entities
        Vector2D moveVector = getNextMoveVector(dt);
        gc.cChecker.checkTile(this, moveVector); // Disable moving if collisions will happen
        gc.cChecker.checkEntity(entities, this, moveVector);

        move(moveVector);
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

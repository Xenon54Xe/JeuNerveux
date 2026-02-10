package com.example.app.entity;

import com.example.app.*;
import com.example.app.entity.group.EntityGroup;
import com.example.app.utils.*;
import com.example.app.utils.collections.List;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Entity implements Updatable, Drawable, Trackable, Collideable {

    // UTILS
    public final GameCanvas gc;

    // STATIC
    public static int NEXT_ID = 0;
    public final static int LEFT_1 = 1;
    public final static int LEFT_2 = 2;
    public final static int RIGHT_1 = 3;
    public final static int RIGHT_2 = 4;

    // CLASS VARIABLES
    protected final int id;
    protected int groupID = EntityGroup.NONE_GROUP_ID;
    protected final String name;

    protected boolean show = true;
    protected boolean active = true;

    protected BufferedImage left1, left2, right1, right2;
    protected int drawDirection = Vector2D.INT_LEFT;

    protected final Vector2D worldPosition = Vector2D.ZERO;
    protected final int width, height;
    protected boolean collisionEnabled;
    protected Rectangle solidArea;

    // PRIVATE VARIABLES
    private int spriteCounter = 0;
    private int spriteNum = 1;
    private final int waitTimeBeforeAnimation = 6;

    public Entity(GameCanvas gc, Rectangle solidArea, String name, int width, int height){
        this.gc = gc;

        this.solidArea = solidArea;
        this.name = name;
        this.width = width;
        this.height = height;
        spriteCounter = (int) (Math.random() * waitTimeBeforeAnimation);

        // ID
        id = NEXT_ID++;
    }

    // CLASS METHODS
    public int getID(){
        return id;
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

    public boolean isShow() {
        return show;
    }

    @Override
    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isVisible(){
        return Drawable.super.isVisible(gc.getTracked(), (int)getWorldX(), (int)getWorldY(), gc.SCREEN_WIDTH, gc.SCREEN_HEIGHT, gc.TILE_SIZE);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // POSITION METHODS
    public Vector2D getWorldPosition() {
        return worldPosition;
    }

    public double getWorldX() {
        // The world pos of the drawn entity's center
        return worldPosition.getX();
    }

    public double getWorldY() {
        // The world pos of the drawn entity's center
        return worldPosition.getY();
    }

    public int getTileX(){
        // Position du centre
        return Vector2D.getTileX(gc.TILE_SIZE, getWorldX());
    }

    public int getTileY(){
        // Position des pieds
        return Vector2D.getTileY(gc.TILE_SIZE, getWorldY() + gc.TILE_SIZE / 2.0);
    }

    public void setWorldPosition(double x, double y) {
        worldPosition.setX(x);
        worldPosition.setY(y);
    }

    public void setTilePosition(int col, int row){
        setWorldPosition(gc.TILE_SIZE * col + getWidth() / 2.0, gc.TILE_SIZE * row + getHeight() / 2.0);
    }

    public void setRandomTilePosition(List<Integer> choiceTiles){
        int x, y;
        int[] rd = Vector2D.chooseRandomTile(gc, choiceTiles);
        x = rd[0];
        y = rd[1];
        // System.out.println(Arrays.toString(rd));
        setTilePosition(x, y);
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

    @Override
    public boolean isCollisionEnabled() {
        return collisionEnabled;
    }

    @Override
    public void setCollisionEnabled(boolean collisionEnabled) {
        this.collisionEnabled = collisionEnabled;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea){
        this.solidArea = solidArea;
    }

    public int getScreenX() {
        return Drawable.super.getScreenX(gc.getTracked(), (int)getWorldX());
    }

    public int getScreenY() {
        return Drawable.super.getScreenY(gc.getTracked(), (int)getWorldY());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public void setSprite(int spriteNum, BufferedImage sprite){
        switch (spriteNum){
            case LEFT_1 -> left1 = sprite;
            case LEFT_2 -> left2 = sprite;
            case RIGHT_1 -> right1 = sprite;
            case RIGHT_2 -> right2 = sprite;
        }
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

    public int getCameraWorldX(){
        return (int) Trackable.super.calcCameraWorldX(gc.SCREEN_WIDTH, gc.tileM.getWorldWidth());
    }

    public int getCameraWorldY(){
        return (int) Trackable.super.calcCameraWorldY(gc.SCREEN_HEIGHT, gc.tileM.getWorldHeight());
    }

    public boolean mouseOver() {
        return Drawable.super.isMouseOver(gc.mouseMH);
    }

    public int getWaitTimeBeforeAnimation() {
        return waitTimeBeforeAnimation;
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

    abstract Entity makeClone();
}

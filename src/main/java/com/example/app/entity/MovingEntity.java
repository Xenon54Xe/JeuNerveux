package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.utils.Vector2D;
import com.example.app.utils.collections.List;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MovingEntity extends Entity{

    protected int baseSpeed;
    protected int currentSpeed = 0;

    protected Vector2D movingDirection = Vector2D.UP;

    public MovingEntity(GameCanvas gc, Rectangle solidArea, String name, int baseSpeed, int width, int height) {
        super(gc, solidArea, name, width, height);

        this.baseSpeed = baseSpeed;
    }

    public void updateDrawDirection(){
        int[] directions = movingDirection.getDirections();
        if (directions == null){
            return;
        }

        for (int direction : directions) {
            if (direction == Vector2D.INT_LEFT) {
                drawDirection = Vector2D.INT_LEFT;
            } else if (direction == Vector2D.INT_RIGHT) {
                drawDirection = Vector2D.INT_RIGHT;
            }
        }
    }

    // MOVE
    public Vector2D getMovingDirection(){
        return movingDirection;
    }

    public void setMoveDirectionVector(double x, double y){
        movingDirection.setX(x);
        movingDirection.setY(y);
        assert movingDirection.isNormalized();
    }

    public void move(Vector2D vector2D) {
        // Make the entity move using its moveVectorDirection
        worldPosition.add(vector2D);
    }

    public void move(double dt) {
        // Anticipate collisions
        Vector2D moveVector = getNextMovingVector(dt);
        gc.cChecker.checkTile(this, moveVector); // Disable moving if collisions will happen

        move(moveVector);
    }

    public void move(List<Entity> entities, double dt){
        // Anticipate collisions with entities
        Vector2D moveVector = getNextMovingVector(dt);
        gc.cChecker.checkTile(this, moveVector); // Disable moving if collisions will happen
        gc.cChecker.checkEntity(entities, this, moveVector);

        move(moveVector);
    }

    public Vector2D getNextMovingVector(double dt){
        Vector2D vec = movingDirection.copy();
        vec.mul(currentSpeed * dt);
        return vec;
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

    @Override
    public void update() {
    }
}

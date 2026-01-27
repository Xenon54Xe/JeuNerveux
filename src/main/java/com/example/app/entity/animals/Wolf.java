package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;

import java.awt.*;

public class Wolf extends Animal{

    public Wolf(GameCanvas gc, String name){
        super(gc, new Rectangle(10, 32, 28, 16), name, 65, gc.TILE_SIZE, gc.TILE_SIZE, 4388, 180, 78, gc.TILE_SIZE, 75, 30);
    }

    public Wolf(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages() {
        left1 = getSpriteImage("entities/animals", "wolf_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "wolf_right.png"); // right
        right2 = right1;
    }

    @Override
    public Entity makeClone() {
        return new Wolf(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), reach, damage, attackDelay);
    }
}

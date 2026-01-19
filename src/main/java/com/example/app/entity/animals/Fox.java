package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;

import java.awt.*;

public class Fox extends Animal{

    public Fox(GameCanvas gc, String name){
        super(gc, new Rectangle(8, 32, 32, 16), name, 50, gc.tileSize, gc.tileSize, 830, 180, 50, gc.tileSize, 150, 30);
    }

    public Fox(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages() {
        left1 = getSpriteImage("entities/animals", "fox_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "fox_right.png"); // right
        right2 = right1;
    }

    @Override
    public Entity makeClone() {
        return new Fox(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), reach, damage, attackDelay);
    }
}

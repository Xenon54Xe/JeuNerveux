package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;

import java.awt.*;

public class Dog extends Animal{

    public Dog(GameCanvas gc, String name){
        super(gc, new Rectangle(8, 32, 32, 16), name, 75, gc.tileSize, gc.tileSize, 910, 180, 35, gc.tileSize, 40, 30);
    }

    public Dog(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages() {
        left1 = getSpriteImage("entities/animals", "dog_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "dog_right.png"); // right
        right2 = right1;
    }

    @Override
    public Entity makeClone() {
        return new Dog(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), getReach(), getDamage(), attackDelay);
    }
}

package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;

import java.awt.*;

public class Rabbit extends Animal{

    public Rabbit(GameCanvas gc, String name){
        super(gc, new Rectangle(8, 40, 32, 8), name, 50, gc.tileSize, gc.tileSize, 423, 180, 5, gc.tileSize, 10, 30);
    }

    public Rabbit(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages() {
        left1 = getSpriteImage("entities/animals", "rabbit_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "rabbit_right.png"); // right
        right2 = right1;
    }

    @Override
    public Entity makeClone() {
        return new Rabbit(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), getReach(), getDamage(), attackDelay);
    }
}

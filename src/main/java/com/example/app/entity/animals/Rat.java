package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.Attack;

import java.awt.*;

public class Rat extends Animal implements Attack {

    public Rat(GameCanvas gc, String name){
        super(gc, new Rectangle(10, 25, 15, 10), name, 50, 35, 35, 231, 180, 2, gc.TILE_SIZE, 6, 30);
    }

    public Rat(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay){
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages(){
        left1 = getSpriteImage("entities/animals", "rat_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "rat_right.png"); // right
        right2 = right1;
    }

    @Override
    public Entity makeClone() {
        return new Rat(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), getReach(), getDamage(), attackDelay);
    }
}

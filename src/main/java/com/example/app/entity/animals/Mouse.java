package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.Attack;
import com.example.app.utils.Vector2D;

import java.awt.*;

public class Mouse extends Animal implements Attack {

    public Mouse(GameCanvas gc, String name){
        super(gc, new Rectangle(6, 20, 12, 4), name, gc.TILE_SIZE / 4, gc.TILE_SIZE / 2, gc.TILE_SIZE / 2, 74, 180, 1, gc.TILE_SIZE, 5, 30);

        setMoveDirectionVector(Vector2D.getRandomVectorNormalized());
        initImages();

        setAvoidWall(true);
    }

    public Mouse(GameCanvas gc, String name, int speed, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, new Rectangle(6, 20, 12, 4), name, speed, gc.TILE_SIZE / 2, gc.TILE_SIZE / 2, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);

        setMoveDirectionVector(Vector2D.getRandomVectorNormalized());
        initImages();

        setAvoidWall(true);
    }

    @Override
    void initImages(){
        left1 = getSpriteImage("entities/animals", "mouse_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "mouse_right.png"); // right
        right2 = right1;
    }



    @Override
    public Entity makeClone() {
        return new Mouse(gc, getName(), getSpeed(), getMaxHealth(), getWaitTimeBeforeAnimation(), getStartXp(), getReach(), getDamage(), attackDelay);
    }
}

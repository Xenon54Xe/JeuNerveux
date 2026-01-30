package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.SceneryManager;
import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;

import java.awt.*;

public class Bear extends Animal{

    public Bear(GameCanvas gc, String name){
        super(gc, new Rectangle(16, 64, 64, 32), name, 65, gc.TILE_SIZE * 2, gc.TILE_SIZE * 2, 22600, 180, 100, gc.TILE_SIZE, 666, 20);
    }

    public Bear(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp, reach, damage, attackDelay);
    }

    @Override
    void initImages() {
        left1 = getSpriteImage("entities/animals", "bear_left.png"); // left
        left2 = left1;
        right1 = getSpriteImage("entities/animals", "bear_right.png"); // right
        right2 = right1;
    }

    @Override
    public void softKill(LivingEntity killer) {
        gc.sceneryM.win();
        super.softKill(killer);
    }

    @Override
    public Entity makeClone() {
        return new Bear(gc, getSolidArea(), getName(), getSpeed(), getWidth(), getHeight(), getMaxHealth(), getWaitTimeBeforeAnimation(), getXp(), reach, damage, attackDelay);
    }
}

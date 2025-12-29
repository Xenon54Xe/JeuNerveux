package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentEntityDead;

import java.awt.*;

public abstract class LivingEntity extends Entity{

    private int health;
    private int xp;

    private boolean dead;

    public LivingEntity(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int xp) {
        super(gc, solidArea, name, speed, width, height);

        this.health = health;
        this.xp = xp;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int xp){
        this.xp += xp;
    }

    public boolean isDead() {
        return dead;
    }

    public void damage(int damage, LivingEntity killer){
        health -= damage;

        if (!dead && health <= 0){
            dead = true;

            if (killer != null) {
                killer.addXp(getXp());
            }
            gc.eventEntityDead.trigger(new ComponentEntityDead(this, killer));
        }
    }
}

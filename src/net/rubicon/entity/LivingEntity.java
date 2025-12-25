package net.rubicon.entity;

import net.rubicon.event.ECEntityDead;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public abstract class LivingEntity extends Entity{

    private int health;
    private int xp;

    private boolean isDead;

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

    public void damage(int damage, LivingEntity killer){
        if (isDead){
            return;
        }

        health -= damage;
        if (health <= 0){
            if (killer != null) {
                killer.addXp(getXp());
            }
            gc.eventEntityDead.trigger(new ECEntityDead(this, killer));
            isDead = true;
        }
    }
}

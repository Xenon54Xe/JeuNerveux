package net.rubicon.entity;

import net.rubicon.event.ECEntityDead;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public abstract class LivingEntity extends Entity{

    private int health;

    public LivingEntity(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health) {
        super(gc, solidArea, name, speed, width, height);

        this.health = health;
    }


    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void damage(int damage){
        health -= damage;
        if (health <= 0){
            gc.eventEntityDead.trigger(new ECEntityDead(this));
        }
    }
}

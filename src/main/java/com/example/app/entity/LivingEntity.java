package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.ui.UIFillBar;
import com.example.app.ui.UIObject;

import java.awt.*;

public abstract class LivingEntity extends Entity {

    // CLASS VARIABLES
    private final int maxHealth;
    private int health;
    private final UIFillBar healthBar;
    private final int startXP;
    private int xp;
    private boolean dead;

    // GROUP
    private int groupID = -1;

    public LivingEntity(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp) {
        super(gc, solidArea, name, speed, width, height, waitTimeBeforeAnimation);

        this.maxHealth = health;
        this.health = health;
        this.startXP = xp;
        this.xp = xp;

        // Health bar
        healthBar = new UIFillBar(gc.mouseMH, Color.GRAY, Color.GREEN, "health bar", 0, 0, width, gc.tileSize / 4, 5, 5);
        healthBar.setDrawReference(UIObject.DRAW_TOP_LEFT_CORNER);
        healthBar.setShow(true);
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getStartXp() {
        return startXP;
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

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    private void kill(LivingEntity killer){
        dead = true;

        if (killer != null) {
            killer.addXp(getXp());
        }
        gc.eventEntityDead.trigger(new ComponentEntityDead(this, killer));
    }

    public void softKill(){
        if (!dead){
            kill(null);
        }
    }

    public void damage(int damage, LivingEntity killer){
        health -= damage;
        healthBar.setPercentFill((double)Math.max(0, health) / maxHealth);

        if (health <= 0){
            kill(killer);
        }
    }

    @Override
    public void update() {
        if (isActive()) {
            healthBar.setScreenPosition(getDrawTopLeftScreenX(), getDrawTopLeftScreenY() + getHeight());
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()){
            healthBar.draw(g2);
        }
    }
}

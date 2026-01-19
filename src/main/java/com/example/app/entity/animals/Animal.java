package com.example.app.entity.animals;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.IAttackEntity;
import com.example.app.entity.LivingEntity;
import com.example.app.utils.Vector2D;

import java.awt.*;

public abstract class Animal extends LivingEntity implements IAttackEntity {

    // ATTACK
    final int attackDelay;
    int attackTimer = 0;
    final int reach;
    final int damage;

    // BEHAVIOR
    int behaviorCount = 0;

    public Animal(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage, int attackDelay) {
        super(gc, solidArea, name, speed, width, height, health, waitTimeBeforeAnimation, xp);

        this.attackDelay = attackDelay;
        this.reach = reach;
        this.damage = damage;

        setMoveDirectionVector(Vector2D.getRandomVectorNormalized());
        initImages();
        setAvoidWall(true);
    }

    public int getReach() {
        return reach;
    }

    public int getDamage() {
        return damage;
    }

    void initImages(){}

    public void baseBehavior(){
        // MOUSE BEHAVIOR
        if (behaviorCount <= 0){
            behaviorCount = 20;

            double amount = (Math.random() - 0.5) / 2 ;

            Vector2D orthogonalVector = new Vector2D(getMoveDirectionVector().getY(), -getMoveDirectionVector().getX()).getNormalized();

            setMoveDirectionVector(getMoveDirectionVector().add(orthogonalVector.mul(amount)).getNormalized());
            updateDrawDirection();
        }
        behaviorCount--;

        move(gc.dt);
        updateShowedSprite();
    }

    @Override
    public void attack() {
        if(attackTimer <= 0) {
            boolean succes = attackFirstNearEnoughEntity(this, gc.entityM.livingEntities, reach, damage);
            if(succes) {
                attackTimer = 20;
            }
        }

        if (attackTimer > 0) {
            attackTimer--;
        }
    }

    @Override
    public void update() {
        if (isActive()){
            super.update();

            if (isOwnBehavior()){
                baseBehavior();
            }

            attack();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isVisible() && isShow()){
            drawWalkingAnimation(g2);

            super.draw(g2);
        }
    }
}

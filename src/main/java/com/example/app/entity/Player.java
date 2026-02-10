package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.entity.group.PlayerEntityGroup;
import com.example.app.event.*;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.handler.KeyHandler;
import com.example.app.utils.FileUtils;
import com.example.app.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends LivingEntity implements Attack, Listener {

    // UTILS
    private final KeyHandler keyH;

    // PLAYER
    private final int sprintSpeed;
    private final int baseSpeed;

    private int reach;
    private int damage;
    private boolean canAttack = true;

    private int attackDelay = 30;
    private int attackTimer = 0;
    private BufferedImage attackImageRight;
    private BufferedImage attackImageLeft;

    // PLAYER GROUP
    public final PlayerEntityGroup playerEntityGroup;

    public Player(GameCanvas gc, String name){
        super(gc, new Rectangle(30, 70, 36, 20), name, 200, gc.TILE_SIZE * 2, gc.TILE_SIZE * 2, 100, 6, 0);

        keyH = gc.keyH;

        sprintSpeed = getSpeed() * 3;
        baseSpeed = getSpeed();
        reach = gc.TILE_SIZE * 2;
        damage = 35;

        // DEFAULT VALUES
        setTilePosition(7, 5);

        initImages();

        // PLAYER ENTITY GROUP
        playerEntityGroup = new PlayerEntityGroup(gc, this);
        gc.entityM.safeAddGroup(playerEntityGroup);

        // Event
        register();
    }

    public Player(GameCanvas gc, String name, int speed, int health, int waitTimeBeforeAnimation, int xp, int reach, int damage){
        super(gc, new Rectangle(8, 32, 32, 16), name, speed, gc.TILE_SIZE, gc.TILE_SIZE, health, waitTimeBeforeAnimation, xp);
        keyH = gc.keyH;

        sprintSpeed = speed * 3;
        baseSpeed = speed;

        this.reach = reach;
        this.damage = damage;

        // DEFAULT VALUES
        setTilePosition(7, 5);

        initImages();

        // PLAYER ENTITY GROUP
        playerEntityGroup = new PlayerEntityGroup(gc, this);
        gc.entityM.safeAddGroup(playerEntityGroup);

        // Event
        register();
    }

    public void initImages(){
        // PLAYER IMAGES
        attackImageRight = FileUtils.getSpriteImage("entities/player", "shaman_attack_right.png");
        attackImageLeft = FileUtils.getSpriteImage("entities/player", "shaman_attack_left.png");
        setSprite(LEFT_1, FileUtils.getSpriteImage("entities/player", "shaman_left1.png"));
        setSprite(LEFT_2, FileUtils.getSpriteImage("entities/player", "shaman_left2.png"));
        setSprite(RIGHT_1, FileUtils.getSpriteImage("entities/player", "shaman_right1.png"));
        setSprite(RIGHT_2, FileUtils.getSpriteImage("entities/player", "shaman_right2.png"));
    }

    public int getReach() {
        return reach;
    }

    public void setReach(int reach) {
        this.reach = reach;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }

    public void playerMoveBehavior(){
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

            double addX = 0, addY = 0;
            if (keyH.upPressed) {
                addY = -1;
            }
            if (keyH.downPressed) {
                addY = 1;
            }
            if (keyH.leftPressed) {
                addX = -1;
            }
            if (keyH.rightPressed) {
                addY = 1;
            }
            double length = Math.sqrt(addX * addX + addY * addY);
            setMoveDirectionVector(addX / length, addY / length);
            updateDrawDirection();

            if (keyH.xPressed && getSpeed() != sprintSpeed) {
                setSpeed(sprintSpeed);
            }
            else if (keyH.cPressed && gc.editorMode){
                setSpeed(sprintSpeed * 3);
            }
            else if (getSpeed() != baseSpeed) {
                setSpeed(baseSpeed);
            }

            // MOVE IF THERE IS NO COLLISIONS (MOVE VECTOR != ZERO)
            move(gc.dt);

            updateShowedSprite();
        }
    }

    public void playerAttackBehavior(){
        if (attackTimer <= 0 && canAttack) {
            attack();
        }
    }

    @Override
    public void update(){
        System.out.println("Player update !!");

        if (gc.mouseH.leftClickClicked){
            canAttack = !canAttack;
        }

        if (isActive()) {
            super.update();

            if (isMoveSpontaneously()) {
                playerMoveBehavior();
            }
            playerAttackBehavior();

            if (!isDead()) {
                playerEntityGroup.update();
            }
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isVisible() && isShow()) {
            // DRAW MAP

            if (attackTimer > 0) {
                // DRAW ATTACK IMAGE
                if (attackTimer > attackDelay / 2) {
                    if (getDrawDirection() == Vector2D.INT_RIGHT) {
                        g2.drawImage(attackImageRight, getScreenX() - getWidth() / 2, getScreenY() - getHeight() / 2, getWidth(), getHeight(), null);
                    }
                    else {
                        g2.drawImage(attackImageLeft, getScreenX() - getWidth() / 2, getScreenY() - getHeight() / 2, getWidth(), getHeight(), null);
                    }
                }
                else {
                    drawWalkingAnimation(g2);
                }
                attackTimer--;
            } else {
                drawWalkingAnimation(g2);
            }

            super.draw(g2);
        }
    }

    @Override
    public void attack() {
        LivingEntity nearest = gc.entityM.getNearestLivingEntity(this, getWorldPosition());
        if (nearest != null && getWorldPosition().getDistance(nearest.getWorldPosition()) <= reach) {
            nearest.damage(damage, this);
            attackTimer = attackDelay;
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity killed, LivingEntity killer)){

            if (killer != null  && killer.getGroupID() == getGroupID()){
                if (killer != this){
                    addXp(killed.getXp());
                }

                // MAKE CLONE
                LivingEntity entity = (LivingEntity) killed.makeClone();
                entity.setWorldPosition(killed.getWorldPosition());
                entity.setSpeed(baseSpeed);
                playerEntityGroup.safeAddEntity(entity);

                if (playerEntityGroup.size() > 10){
                    LivingEntity weekest = playerEntityGroup.getWeakest();
                    playerEntityGroup.safeRemoveEntity(weekest);
                    weekest.softKill(null);
                    return;
                }
            }

            if (killed.getGroupID() == getGroupID() && !killed.equals(this)){
                playerEntityGroup.safeAddEntity(killed);
            }
        }
    }

    @Override
    public void register() {
        gc.eventEntityDead.addListener(this);
    }

    @Override
    public Entity makeClone() {
        return new Player(gc, getName(), baseSpeed, getMaxHealth(), getWaitTimeBeforeAnimation(), getStartXp(), reach, damage);
    }
}

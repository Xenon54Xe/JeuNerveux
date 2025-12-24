package net.rubicon.entity;

import net.rubicon.handler.MouseHandler;
import net.rubicon.main.GameCanvas;
import net.rubicon.handler.KeyHandler;
import net.rubicon.utils.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends LivingEntity implements IAttackEntity{

    // UTILS
    private final KeyHandler keyH;
    private final MouseHandler mouseH;

    // PLAYER
    private final int sprintSpeed;
    private final int baseSpeed;

    private final int reach;
    private final int damage;
    private int attackTimer = 0;
    private BufferedImage attackImage;

    public Player(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int reach, int damage){
        super(gc, solidArea, name, speed, width, height, health);

        keyH = gc.keyH;
        mouseH = gc.mouseH;

        sprintSpeed = speed * 3;
        baseSpeed = speed;

        this.reach = reach;
        this.damage = damage;

        // DEFAULT VALUES
        setWorldPosition(new Vector2D(gc.tileSize * 7, gc.tileSize * 5));

        // PLAYER IMAGES
        attackImage = getSpriteImage("/res/entities/player/boy_down_sword.png");
        up1 = getSpriteImage("/res/entities/player/boy_up_1.png");
        up2 = getSpriteImage("/res/entities/player/boy_up_2.png");
        down1 = getSpriteImage("/res/entities/player/boy_down_1.png");
        down2 = getSpriteImage("/res/entities/player/boy_down_2.png");
        left1 = getSpriteImage("/res/entities/player/boy_left_1.png");
        left2 = getSpriteImage("/res/entities/player/boy_left_2.png");
        right1 = getSpriteImage("/res/entities/player/boy_right_1.png");
        right2 = getSpriteImage("/res/entities/player/boy_right_2.png");

    }

    @Override
    public void update(double dt){
        if (moveVector != Vector2D.ZERO) {
            setMoveVector(Vector2D.ZERO);
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            if (keyH.upPressed){
                addMoveVector(Vector2D.UP);
            }
            if (keyH.downPressed){
                addMoveVector(Vector2D.DOWN);
            }
            if (keyH.leftPressed){
                addMoveVector(Vector2D.LEFT);
            }
            if (keyH.rightPressed){
                addMoveVector(Vector2D.RIGHT);
            }

            if (keyH.xPressed && getSpeed() != sprintSpeed){
                setSpeed(sprintSpeed);
            }
            else if (getSpeed() != baseSpeed){
                setSpeed(baseSpeed);
            }

            gc.cChecker.checkTile(this);

            updateDrawDirection();

            // MOVE IF THERE IS NO COLLISIONS
            move(moveVector.getNormalized().mul(getSpeed()).mul(dt));

            updateShowedSprite();
        }

        if (attackTimer == 0 && mouseH.leftClickClicked){
            attack();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()) {
            if (attackTimer > 0) {
                // DRAW ATTACK IMAGE
                g2.drawImage(attackImage, getScreenX() - getWidth() / 2, getScreenY() - getHeight() / 2, gc.tileSize, gc.tileSize, null);
                attackTimer--;
            } else {

                BufferedImage image = null;

                switch (getDrawDirection()) {
                    case "up":
                        if (getSpriteNum() == 1) {
                            image = getSprite("up1");
                        }
                        if (getSpriteNum() == 2) {
                            image = getSprite("up2");
                        }
                        break;
                    case "down":
                        if (getSpriteNum() == 1) {
                            image = getSprite("down1");
                        }
                        if (getSpriteNum() == 2) {
                            image = getSprite("down2");
                        }
                        break;
                    case "left":
                        if (getSpriteNum() == 1) {
                            image = getSprite("left1");
                        }
                        if (getSpriteNum() == 2) {
                            image = getSprite("left2");
                        }
                        break;
                    case "right":
                        if (getSpriteNum() == 1) {
                            image = getSprite("right1");
                        }
                        if (getSpriteNum() == 2) {
                            image = getSprite("right2");
                        }
                        break;
                }

                g2.drawImage(image, getScreenX() - getWidth() / 2, getScreenY() - getHeight() / 2, gc.tileSize, gc.tileSize, null);
            }
        }
    }

    @Override
    public void attack() {
        attackNearestEntity(this, gc.entityM.livingEntities, reach, damage);
        attackTimer = 10;
    }
}

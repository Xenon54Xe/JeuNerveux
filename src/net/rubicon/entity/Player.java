package net.rubicon.entity;

import net.rubicon.handler.MouseHandler;
import net.rubicon.main.GameCanvas;
import net.rubicon.handler.KeyHandler;
import net.rubicon.utils.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends LivingEntity implements IAttackEntity{

    // UTILS
    private final KeyHandler keyH;
    private final MouseHandler mouseH;

    // PLAYER
    private final int sprintSpeed;
    private final int baseSpeed;

    private final int reach;
    private final int damage;

    private int attackDelay = 10;
    private int attackTimer = 0;
    private BufferedImage attackImage;

    public Player(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int xp, int reach, int damage){
        super(gc, solidArea, name, speed, width, height, health, xp);

        keyH = gc.keyH;
        mouseH = gc.mouseH;

        sprintSpeed = speed * 3;
        baseSpeed = speed;

        this.reach = reach;
        this.damage = damage;

        // DEFAULT VALUES
        setTilePosition(7, 5);

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
        if (isActive()) {
            if (getMoveDirectionVector() != Vector2D.ZERO) {
                setMoveDirectionVector(Vector2D.ZERO);
            }

            if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {

                Vector2D newMoveVector = Vector2D.ZERO;
                if (keyH.upPressed) {
                    newMoveVector = newMoveVector.add(Vector2D.UP);
                }
                if (keyH.downPressed) {
                    newMoveVector = newMoveVector.add(Vector2D.DOWN);
                }
                if (keyH.leftPressed) {
                    newMoveVector = newMoveVector.add(Vector2D.LEFT);
                }
                if (keyH.rightPressed) {
                    newMoveVector = newMoveVector.add(Vector2D.RIGHT);
                }
                setMoveDirectionVector(newMoveVector.getNormalized());

                if (keyH.xPressed && getSpeed() != sprintSpeed) {
                    setSpeed(sprintSpeed);
                } else if (getSpeed() != baseSpeed) {
                    setSpeed(baseSpeed);
                }

                gc.cChecker.checkTile(this);

                updateDrawDirection();

                // MOVE IF THERE IS NO COLLISIONS (MOVE VECTOR != ZERO)
                move(dt);

                updateShowedSprite();
            }

            if (attackTimer == 0 && mouseH.leftClickClicked && !gc.uiM.isMouseOverUI()) {
                attack();
            }
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

                drawWalkingAnimation(g2);
            }
        }
    }

    @Override
    public void attack() {
        attackNearestEntity(this, gc.entityM.livingEntities, reach, damage);
        attackTimer = attackDelay;
    }
}

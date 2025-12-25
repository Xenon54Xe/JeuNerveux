package net.rubicon.entity;

import net.rubicon.UI.DrawVector;
import net.rubicon.main.GameCanvas;
import net.rubicon.utils.Vector2D;

import java.awt.*;

public class Mouse extends LivingEntity implements IAttackEntity{

    double amount = 0;
    int count = 0;

    // ATTACK
    private int attackTimer = 0;
    private final int reach;
    private final int damage;

    Vector2D moveVector = Vector2D.getRandomVectorNormalized();

    public Mouse(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health, int xp, int reach, int damage) {
        super(gc, solidArea, name, speed, width, height, health, xp);

        setMoveDirectionVector(Vector2D.getRandomVectorNormalized());

        up1 = getSpriteImage("/res/entities/mouse/mouse_left.png");
        up2 = getSpriteImage("/res/entities/mouse/mouse_right.png");
        down1 = up1;
        left1 = up1;
        left2 = up1;
        down2 = up2;
        right1 = up2;
        right2 = up2;

        this.reach = reach;
        this.damage = damage;
    }

    @Override
    public void update(double dt) {
        if (isActive()){

            // MOUSE BEHAVIOR
            if (count <= 0){
                count = 20;

                amount = (Math.random() - 0.5) / 2 ;

                Vector2D orthogonalVector = new Vector2D(moveVector.getY(), -moveVector.getX()).getNormalized();
                moveVector = moveVector.add(orthogonalVector.mul(amount)).getNormalized();

                setMoveDirectionVector(moveVector);

            }
            count--;

            gc.cChecker.checkTile(this);

            move(dt);

            attack();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (isShow()){
            drawWalkingAnimation(g2);

            DrawVector vector = new DrawVector(getScreenPosition(), moveVector.mul(100), Color.BLUE);
            vector.draw(g2);
        }
    }

    @Override
    public void attack() {
        if(attackTimer <= 0) {
            attackFirstNearEnoughEntity(this, gc.entityM.livingEntities, reach, damage);
            attackTimer = 20;
        }
        attackTimer--;
    }
}

package net.rubicon.entity;

import net.rubicon.main.GameCanvas;

import java.awt.*;

public class Mouse extends LivingEntity implements IAttackEntity{

    public Mouse(GameCanvas gc, Rectangle solidArea, String name, int speed, int width, int height, int health) {
        super(gc, solidArea, name, speed, width, height, health);

        up1 = getSpriteImage("/res/entities/mouse/mouse_left.png");
        up2 = getSpriteImage("/res/entities/mouse/mouse_right.png");
        down1 = up1;
        left1 = up1;
        left2 = up1;
        down2 = up2;
        right1 = up2;
        right2 = up2;
    }

    @Override
    public void update(double dt) {

    }

    @Override
    public void draw(Graphics2D g2) {

    }

    @Override
    public void attack() {

    }
}

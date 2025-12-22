package net.rubicon.entity;

import net.rubicon.main.GameCanvas;
import net.rubicon.handler.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{

    private final GameCanvas gc;
    private final KeyHandler keyH;

    public final int screenX;
    public final int screenY;

    private int sprintSpeed;
    private int baseSpeed;

    public Player(GameCanvas gc){
        this.gc = gc;
        this.keyH = gc.keyH;

        screenX = gc.screenWidth / 2 - gc.tileSize / 2;
        screenY = gc.screenHeight / 2 - gc.tileSize / 2;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        setWorldX(gc.tileSize * 7);
        setWorldY(gc.tileSize * 5);
        setSpeed(200);

        setDirection("down");

        sprintSpeed = getSpeed() * 3;
        baseSpeed = getSpeed();
    }

    public void getPlayerImage(){

        // GET PLAYER IMAGE
        try {

            setSprite("up1", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_up_1.png"))));
            setSprite("up2", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_up_2.png"))));
            setSprite("down1", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_down_1.png"))));
            setSprite("down2", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_down_2.png"))));
            setSprite("left1", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_left_1.png"))));
            setSprite("left2", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_left_2.png"))));
            setSprite("right1", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_right_1.png"))));
            setSprite("right2", ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/player/boy_right_2.png"))));

        }catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void update(double dt){
        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            if (keyH.upPressed){
                setDirection("up");
                addWorldY(-getSpeed() * dt);
            }
            if (keyH.downPressed){
                setDirection("down");
                addWorldY(getSpeed() * dt);
            }
            if (keyH.leftPressed){
                setDirection("left");
                addWorldX(-getSpeed() * dt);
            }
            if (keyH.rightPressed){
                setDirection("right");
                addWorldX(getSpeed() * dt);
            }

            if (keyH.xPressed){
                setSpeed(sprintSpeed);
            }
            else if (getSpeed() != baseSpeed){
                setSpeed(baseSpeed);
            }

            updateShowedSprite();
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch (getDirection()){
            case "up":
                if (getSpriteNum() == 1) {
                    image = getSprite("up1");
                }
                if (getSpriteNum() == 2){
                    image = getSprite("up2");
                }
                break;
            case "down":
                if (getSpriteNum() == 1){
                    image = getSprite("down1");
                }
                if (getSpriteNum() == 2){
                    image = getSprite("down2");
                }
                break;
            case "left":
                if (getSpriteNum() == 1) {
                    image = getSprite("left1");
                }
                if (getSpriteNum() == 2){
                    image = getSprite("left2");
                }
                break;
            case "right":
                if (getSpriteNum() == 1) {
                    image = getSprite("right1");
                }
                if (getSpriteNum() == 2){
                    image = getSprite("right2");
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gc.tileSize, gc.tileSize, null);
    }
}

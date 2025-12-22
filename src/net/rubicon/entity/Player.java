package net.rubicon.entity;

import net.rubicon.main.GameCanvas;
import net.rubicon.handler.KeyHandler;
import net.rubicon.utils.Vector2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player extends Entity{

    // UTILS
    private final KeyHandler keyH;

    // CLASS VARIABLES
    public final int screenX;
    public final int screenY;

    private int sprintSpeed;
    private int baseSpeed;

    public Player(GameCanvas gc){
        super(gc);

        this.keyH = gc.keyH;

        screenX = gc.screenWidth / 2 - gc.tileSize / 2;
        screenY = gc.screenHeight / 2 - gc.tileSize / 2;

        solidArea = new Rectangle(8, 16, 32, 32);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        setWorldPosition(new Vector2D(gc.tileSize * 7, gc.tileSize * 5));
        setSpeed(200);

        setDrawDirection("down");

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
    }

    @Override
    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch (getDrawDirection()){
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

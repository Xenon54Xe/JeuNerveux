package com.example.app.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

    public final static int TRANSPARENT = 0;
    public final static int GREEN = 1;
    public final static int DARK_GREEN = 2;
    public final static int GRAY = 3;
    public final static int BROWN = 4;
    public final static int YELLOW = 5;
    public final static int ORANGE = 6;
    public final static int BLUE = 7;

    // CLASS VARIABLES
    private final BufferedImage image;
    private final int colorNum;
    private final boolean collision;

    // Match with the numbers in the map
    private int ID;

    public Tile(BufferedImage image, int colorNum){
        this.image = image;
        this.colorNum = colorNum;
        collision = false;
    }

    public Tile(BufferedImage image, int colorNum, boolean collision){
        this.image = image;
        this.colorNum = colorNum;
        this.collision = collision;
    }

    public static Color getRelatedColor(int colorNum){
        return switch (colorNum){
            case TRANSPARENT -> Color.BLACK;
            case GREEN -> Color.GREEN;
            case DARK_GREEN -> new Color(13, 78, 0);
            case GRAY -> Color.GRAY;
            case BROWN -> new Color(58, 5, 5);
            case YELLOW -> Color.YELLOW;
            case ORANGE -> Color.ORANGE;
            case BLUE -> Color.BLUE;
            default -> throw new IllegalStateException("Unexpected value: " + colorNum);
        };
    }

    public BufferedImage getImage() {
        return image;
    }

    public int getColorNum() {
        return colorNum;
    }

    public boolean isCollision() {
        return collision;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

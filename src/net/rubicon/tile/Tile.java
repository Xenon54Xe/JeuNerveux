package net.rubicon.tile;

import java.awt.image.BufferedImage;

public class Tile {

    public BufferedImage image;
    public boolean transparent;
    public boolean collision;

    // Match with the numbers in the map
    private int ID;

    public Tile(BufferedImage image){
        this.image = image;
        transparent = false;
        collision = false;
    }

    public Tile(BufferedImage image, boolean transparent, boolean collision){
        this.image = image;
        this.transparent = transparent;
        this.collision = collision;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

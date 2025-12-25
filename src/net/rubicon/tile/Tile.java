package net.rubicon.tile;

import java.awt.image.BufferedImage;

public class Tile {

    private final BufferedImage image;
    private final boolean collision;

    private final int[] authorizedLayers;

    // Match with the numbers in the map
    private int ID;

    public Tile(BufferedImage image, int... layers){
        this.image = image;
        collision = false;
        authorizedLayers = layers;
    }

    public Tile(BufferedImage image, boolean collision, int... layers){
        this.image = image;
        this.collision = collision;

        authorizedLayers = layers;
    }

    public BufferedImage getImage() {
        return image;
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

    public boolean layerContains(int layer){
        for (int cur : authorizedLayers){
            if (cur == layer){
                return true;
            }
        }
        return false;
    }
}

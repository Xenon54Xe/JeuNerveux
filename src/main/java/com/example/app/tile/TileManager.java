package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentChangeMap;
import com.example.app.utils.FileUtils;
import com.example.app.utils.TileLinkedList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {

    final GameCanvas gc;

    // WORLD VARIABLES
    private int maxWorldCol, maxWorldRow, layerCount;
    private int maxWorldWidth, maxWorldHeight;

    // CLASS VARIABLES
    public final TileLinkedList tiles = new TileLinkedList();
    private String mapName;
    public int[][][] tileMapNum;

    // If the map is 50x50 the 50th com.example.app.tile will be nbCol=0, nbRow = 1...
    public ArrayList<Integer> spawnableTiles = new ArrayList<>();

    // LOAD MAP LATER
    private boolean willLoadMap = false;
    private int waitLoadMapCount = 0;

    public TileManager(GameCanvas gc){
        this.gc = gc;

        // Tile types
        getTileImage();
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapName() {
        return mapName;
    }

    public int getMaxWorldCol() {
        return maxWorldCol;
    }

    public int getMaxWorldRow() {
        return maxWorldRow;
    }

    public int getWorldWidth() {
        return maxWorldWidth;
    }

    public int getWorldHeight() {
        return maxWorldHeight;
    }

    public int getLayerCount() {
        return layerCount;
    }

    private void getTileImage(){

        try {
            // Complete tiles
            // THE FIRST TILE MUST BE TRANSPARENT (SKIPPED WHEN DRAWN)
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "transparent.png"))),0, 1, 2));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "grass.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "wall.png"))), true, 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "earth.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "sand.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "water.png"))), true, 1, 2));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_cross.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_horizontal.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_vertical.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "tree.png"))), true, 1, 2));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void findSpawnableTiles(){
        spawnableTiles.clear();

        for (int row = 0; row < maxWorldRow; row++) {

            for (int col = 0; col < maxWorldCol; col++) {

                boolean spawnable = true;
                for (int layer = 0; layer < layerCount; layer++) {

                    if (tiles.getTile(tileMapNum[col][row][layer]).isCollision()){
                        spawnable = false;
                    }
                }
                if (spawnable){
                    int number = col + maxWorldCol * row;
                    spawnableTiles.add(number);
                }
            }
        }
    }

    public int[] getMapDimensions(){
        return FileUtils.getMapDimensions(mapName);
    }

    public void loadMap(){
        assert mapName != null;

        // VARIABLES
        int[] dimensions = getMapDimensions();
        maxWorldCol = dimensions[0];
        maxWorldRow = dimensions[1];
        layerCount = dimensions[2];
        tileMapNum = new int[maxWorldCol][maxWorldRow][layerCount];

        maxWorldWidth = maxWorldCol * gc.tileSize;
        maxWorldHeight = maxWorldRow * gc.tileSize;

        // LOAD MAP
        FileUtils.loadMap(tileMapNum, mapName);
        findSpawnableTiles();

        gc.eventChangeMap.trigger(new ComponentChangeMap(mapName, spawnableTiles));
    }

    public void setTileMapNum(int[][][] tileMapNum) {
        maxWorldCol = tileMapNum.length;
        maxWorldRow = tileMapNum[0].length;
        layerCount = tileMapNum[0][0].length;

        maxWorldWidth = maxWorldCol * gc.tileSize;
        maxWorldHeight = maxWorldRow * gc.tileSize;

        this.tileMapNum = tileMapNum;
    }

    public void draw(Graphics2D g2){
        assert tileMapNum != null;

        int worldCol = 0;
        int worldRow = 0;

        double cameraWorldX = gc.tracked.getCameraWorldX();
        double cameraWorldY = gc.tracked.getCameraWorldY();

        while (worldCol < maxWorldCol && worldRow < maxWorldRow){

            // GET WHERE TILE WILL BE DRAWN
            int worldX = worldCol * gc.tileSize;
            int worldY = worldRow * gc.tileSize;

            int screenX = (int)(worldX - cameraWorldX);
            int screenY = (int)(worldY - cameraWorldY);

            // DRAW IF TILE IN SCREEN
            if (worldX > cameraWorldX - gc.tileSize
                    && worldX < cameraWorldX + gc.screenWidth + gc.tileSize
                    && worldY > cameraWorldY - gc.tileSize
                    && worldY < cameraWorldY + gc.screenHeight + gc.tileSize){

                for (int layer = 0; layer < layerCount; layer++) {

                    // GET TILE ID
                    int tileID = tileMapNum[worldCol][worldRow][layer];
                    if (tileID != 0) {
                        g2.drawImage(tiles.getTile(tileID).getImage(), screenX, screenY, gc.tileSize, gc.tileSize, null);
                    }
                }
            }

            worldCol++;
            if (worldCol == maxWorldCol){

                worldCol = 0;
                worldRow++;
            }
        }
    }
}

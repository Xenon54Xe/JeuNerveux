package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.component.ComponentChangeMap;
import com.example.app.utils.*;
import com.example.app.utils.collections.List;
import com.example.app.utils.collections.LinkedList;
import com.example.app.utils.collections.TileLoopList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class TileManager {

    final GameCanvas gc;

    // WORLD VARIABLES
    private int maxWorldCol, maxWorldRow, layerCount;
    private int maxWorldWidth, maxWorldHeight;

    // CLASS VARIABLES
    public final TileLoopList tiles = new TileLoopList();
    private String mapName;
    public TileMap tileMap;

    // If the map is 50x50 the 50th com.example.app.tile will be nbCol=0, nbRow = 1...
    public List<Integer> spawnableTiles = new LinkedList<>();

    // LOAD MAP LATER
    private boolean willLoadMap = false;
    private int waitLoadMapCount = 0;

    public TileManager(GameCanvas gc){
        this.gc = gc;

        // Tile types
        getTileImage();
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
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "transparent.png"))), Tile.TRANSPARENT));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "grass.png"))), Tile.GREEN));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "wall.png"))), Tile.GRAY, true));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "earth.png"))), Tile.BROWN));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "sand.png"))), Tile.YELLOW));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "water.png"))), Tile.BLUE, true));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_cross.png"))), Tile.ORANGE));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_horizontal.png"))), Tile.ORANGE));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "path_vertical.png"))), Tile.ORANGE));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(FileUtils.loadFile("tiles", "tree.png"))), Tile.DARK_GREEN, true));

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

                    if (tiles.getTile(tileMap.getTileNum(col, row, layer)).isCollision()){
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

    public void loadMap(String mapName){
        this.mapName = mapName;
        loadMap();
    }

    public void loadMap(){
        assert mapName != null;

        // VARIABLES
        int[] dimensions = getMapDimensions();
        maxWorldCol = dimensions[0];
        maxWorldRow = dimensions[1];
        layerCount = dimensions[2];
        tileMap = new TileMap(maxWorldCol, maxWorldRow, layerCount);

        maxWorldWidth = maxWorldCol * gc.TILE_SIZE;
        maxWorldHeight = maxWorldRow * gc.TILE_SIZE;

        // LOAD MAP
        FileUtils.loadMap(tileMap, mapName);
        findSpawnableTiles();

        gc.eventChangeMap.trigger(new ComponentChangeMap(mapName, spawnableTiles));

//        // MAP
//        gc.uiM.uiMap.initMap(tileMap);
    }

    public void setTileMapNum(int[][][] tileMapNum) {
        tileMap = new TileMap(tileMapNum);

        maxWorldCol = tileMap.getMaxCol();
        maxWorldRow = tileMap.getMaxRow();
        layerCount = tileMap.getLayerCount();

        maxWorldWidth = maxWorldCol * gc.TILE_SIZE;
        maxWorldHeight = maxWorldRow * gc.TILE_SIZE;
    }

    public void draw(Graphics2D g2){
        assert tileMap != null;

        double cameraWorldX, cameraWorldY;
        cameraWorldX = gc.getTracked().getCameraWorldX();
        cameraWorldY = gc.getTracked().getCameraWorldY();

        int startWorldCol, endWorldCol, startWorldRow, endWorldRow;
        startWorldCol = Math.max(
                0,
                Vector2D.getTileX(gc.TILE_SIZE, cameraWorldX));
        endWorldCol = Math.min(
                maxWorldCol,
                Vector2D.getTileX(gc.TILE_SIZE, cameraWorldX + gc.SCREEN_WIDTH + gc.TILE_SIZE));
        startWorldRow = Math.max(
                0,
                Vector2D.getTileY(gc.TILE_SIZE, cameraWorldY));
        endWorldRow = Math.min(
                maxWorldRow,
                Vector2D.getTileY(gc.TILE_SIZE, cameraWorldY + gc.SCREEN_HEIGHT + gc.TILE_SIZE));

        int worldCol = startWorldCol;
        int worldRow = startWorldRow;
        while (worldCol < endWorldCol && worldRow < endWorldRow){

            // GET WHERE TILE WILL BE DRAWN
            int worldX = worldCol * gc.TILE_SIZE;
            int worldY = worldRow * gc.TILE_SIZE;

            int screenX = (int)(worldX - cameraWorldX);
            int screenY = (int)(worldY - cameraWorldY);

            // DRAW
            for (int layer = 0; layer < layerCount; layer++) {

                // GET TILE ID
                int tileID = tileMap.getTileNum(worldCol, worldRow, layer);
                if (tileID != 0) {
                    g2.drawImage(tiles.getTile(tileID).getImage(), screenX, screenY, gc.TILE_SIZE, gc.TILE_SIZE, null);
                }
            }

            worldCol++;
            if (worldCol == endWorldCol){

                worldCol = 0;
                worldRow++;
            }
        }
    }
}

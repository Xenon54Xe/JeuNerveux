package net.rubicon.tile;

import net.rubicon.event.ComponentChangeMap;
import net.rubicon.main.GameCanvas;
import net.rubicon.utils.TileLinkedList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class TileManager {

    // WORLD VARIABLES
    private int maxWorldCol, maxWorldRow, layerCount;
    private int maxWorldWidth, maxWorldHeight;

    // CLASS VARIABLES
    public final TileLinkedList tiles = new TileLinkedList();
    private String mapName;
    public int[][][] tileMapNum;

    // If the map is 50x50 the 50th tile will be col=0, row = 1...
    public ArrayList<Integer> spawnableTiles = new ArrayList<>();

    // UTILS
    GameCanvas gc;

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
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/transparent.png"))),0, 1, 2));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/grass.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/wall.png"))), true, 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/earth.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/sand.png"))), 0));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/water.png"))), true, 1, 2));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/path_cross.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/path_horizontal.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/path_vertical.png"))), 1));

            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/tree.png"))), true, 1, 2));

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

    private int[] getMapDimensions(String filePath){
        // COL, ROW, LAYER
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            // GET THE LINE AND FORMAT IT
            while (line.contains("  ")) {
                line = line.replace("  ", " ");
            }
            while (line.startsWith(" ")){
                line = line.substring(1);
            }
            while (line.endsWith(" ")){
                line = line.substring(0, line.length() - 1);
            }

            // GET THE STRINGS FOR EVERY TILES
            String[] lineLayerNumbers = line.split(" ");

            // GET THE NUMBERS FOR EVERY LAYER OF A TILE
            String[] tileLayerNumbers = lineLayerNumbers[0].split(":");

            // GET THE NUMBER OF LINES
            int lineCount = 1;
            while (br.readLine() != null){
                lineCount++;
            }

            return new int[]{lineLayerNumbers.length, lineCount, tileLayerNumbers.length};

        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }

    public int[] getMapDimensions(){
        return getMapDimensions("/res/maps/" + mapName);
    }

    private void loadMap(int[][][] tileMapNum, String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int maxWorldRow = tileMapNum.length;
            int maxWorldCol = tileMapNum[0].length;
            int layerCount = tileMapNum[0][0].length;

            int col = 0;
            int row = 0;

            while (col < maxWorldCol && row < maxWorldRow){

                // GET THE LINE AND FORMAT IT
                String line = br.readLine();
                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }
                while (line.startsWith(" ")){
                    line = line.substring(1);
                }

                // GET THE STRINGS FOR EVERY TILES
                String[] lineLayerNumbers = line.split(" ");

                while (col < maxWorldCol){

                    // GET THE NUMBERS FOR EVERY LAYER OF A TILE
                    String[] tileLayerNumbers = lineLayerNumbers[col].split(":");
                    for (int layer = 0; layer < layerCount; layer++) {

                        // LAYER VALUE
                        int num = Integer.parseInt(tileLayerNumbers[layer]);
                        tileMapNum[col][row][layer] = num;
                    }

                    col++;
                }

                if (col == maxWorldCol){
                    col = 0;
                    row++;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
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
        loadMap(tileMapNum, "/res/maps/" + mapName);
        findSpawnableTiles();

        gc.eventChangeMap.trigger(new ComponentChangeMap(mapName, spawnableTiles));
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

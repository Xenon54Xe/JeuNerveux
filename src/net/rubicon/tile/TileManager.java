package net.rubicon.tile;

import net.rubicon.main.GameCanvas;
import net.rubicon.utils.LinkedList;
import net.rubicon.utils.TileLinkedList;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;

public class TileManager {

    // CLASS VARIABLES
    public final TileLinkedList tiles = new TileLinkedList();
    public String mapName;
    public int[][][] tileMapNum;

    // UTILS
    GameCanvas gc;

    public TileManager(GameCanvas gc, String mapName){
        this.gc = gc;
        this.mapName = mapName;

        // Tile types
        getTileImage();

        // Load map
        tileMapNum = new int[gc.maxWorldCol][gc.maxWorldRow][gc.layerCount];
        loadMap("/res/maps/" + mapName + ".txt");
    }

    private void getTileImage(){

        try {
            // Complete tiles
            // THE FIRST TILE MUST BE TRANSPARENT (SKIPPED WHEN DRAWN)
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().
                    getResourceAsStream("/res/tiles/transparent.png"))), 1, 2));

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

    public void loadMap(String filePath){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gc.maxWorldCol && row < gc.maxWorldRow){

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

                while (col < gc.maxWorldCol){

                    // GET THE NUMBERS FOR EVERY LAYER OF A TILE
                    String[] tileLayerNumbers = lineLayerNumbers[col].split(":");
                    for (int layer = 0; layer < tileLayerNumbers.length; layer++) {

                        // LAYER VALUE
                        int num = Integer.parseInt(tileLayerNumbers[layer]);
                        tileMapNum[col][row][layer] = num;
                    }

                    col++;
                }

                if (col == gc.maxWorldCol){
                    col = 0;
                    row++;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gc.maxWorldCol && worldRow < gc.maxWorldRow){

            // GET WHERE TILE WILL BE DRAWN
            int worldX = worldCol * gc.tileSize;
            int worldY = worldRow * gc.tileSize;
            int screenX = (int)(worldX - gc.player.getWorldX() + gc.player.screenX);
            int screenY = (int)(worldY - gc.player.getWorldY() + gc.player.screenY);

            // DRAW IF TILE IN SCREEN
            if (worldX > gc.player.getWorldX() - gc.player.screenX - gc.tileSize
                    && worldX < gc.player.getWorldX() + gc.player.screenX + gc.tileSize
                    && worldY > gc.player.getWorldY() - gc.player.screenY - gc.tileSize
                    && worldY < gc.player.getWorldY() + gc.player.screenY + gc.tileSize){

                for (int layer = 0; layer < gc.layerCount; layer++) {

                    // GET TILE ID
                    int tileID = tileMapNum[worldCol][worldRow][layer];
                    if (tileID != 0) {
                        g2.drawImage(tiles.getTile(tileID).getImage(), screenX, screenY, gc.tileSize, gc.tileSize, null);
                    }
                }
            }

            worldCol++;
            if (worldCol == gc.maxWorldCol){

                worldCol = 0;
                worldRow++;
            }
        }
    }
}

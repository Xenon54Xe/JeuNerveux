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

    // For full tiles
    int[][] mapCompleteTileNum;
    // For path, water, ...
    int[][] mapTransparentTileNum;

    public String mapName;

    // UTILS
    GameCanvas gc;

    public TileManager(GameCanvas gc, String mapName){
        this.gc = gc;
        this.mapName = mapName;

        // Tiles
        mapCompleteTileNum = new int[gc.maxWorldCol][gc.maxWorldRow];
        mapTransparentTileNum = new int[gc.maxWorldCol][gc.maxWorldRow];
        getTileImage();

        // Load map
        loadMap("/res/maps/" + mapName);
    }

    private void getTileImage(){

        try {
            // Complete tiles
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/transparent.png"))), true, false));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/grass.png")))));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/wall.png"))), false, true));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/earth.png")))));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/sand.png")))));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/water.png"))), true, false));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/path_cross.png"))), true, false));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/path_horizontal.png"))), true, false));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/path_vertical.png"))), true, false));
            tiles.add(new Tile(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/res/tiles/tree.png"))), true, true));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadTiles(String filePath, boolean transparent){
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gc.maxWorldCol && row < gc.maxWorldRow){

                String line = br.readLine();
                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }
                while (line.startsWith(" ")){
                    line = line.substring(1);
                }

                String[] numbers = line.split(" ");

                while (col < gc.maxWorldCol){

                    int num = Integer.parseInt(numbers[col]);

                    if (!transparent) {
                        mapCompleteTileNum[col][row] = num % (tiles.size());
                    } else {
                        mapTransparentTileNum[col][row] = num % (tiles.size());
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

    public void loadMap(String filePath){

        loadTiles(filePath + ".txt", false);
        loadTiles(filePath + "-t.txt", true);
    }

    private void drawTiles(Graphics2D g2, int[][] mapTileNum){
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gc.maxWorldCol && worldRow < gc.maxWorldRow){
            // GET TILE ID
            int tileID = mapTileNum[worldCol][worldRow];

            // GET WHERE TILE WILL BE DRAWN
            int worldX = worldCol * gc.tileSize;
            int worldY = worldRow * gc.tileSize;
            int screenX = (int)(worldX - gc.player.getWorldX() + gc.player.screenX);
            int screenY = (int)(worldY - gc.player.getWorldY() + gc.player.screenY);

            if (tileID >= 0
                    && worldX > gc.player.getWorldX() - gc.player.screenX - gc.tileSize
                    && worldX < gc.player.getWorldX() + gc.player.screenX + gc.tileSize
                    && worldY > gc.player.getWorldY() - gc.player.screenY - gc.tileSize
                    && worldY < gc.player.getWorldY() + gc.player.screenY + gc.tileSize){
                g2.drawImage(tiles.getTile(tileID).image, screenX, screenY, gc.tileSize, gc.tileSize, null);
            }

            worldCol++;

            if (worldCol == gc.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public void draw(Graphics2D g2){

        drawTiles(g2, mapCompleteTileNum);
        drawTiles(g2, mapTransparentTileNum);
    }
}

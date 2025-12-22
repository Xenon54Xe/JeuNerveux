package net.rubicon.tile.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LoadOldMap {

    public int[][] mapCompleteTileNum;
    public int[][] mapTransparentTileNum;

    public void loadMap(String fileName, int width, int height){
        loadTiles("/res/maps/" + fileName + ".txt", false, width, height);
        loadTiles("/res/maps/" + fileName + "-t.txt", true, width, height);
    }

    private void loadTiles(String filePath, boolean transparent, int width, int height){
        try {
            InputStream is;
            if (!transparent) {
                is = getClass().getResourceAsStream(filePath);
                mapCompleteTileNum = new int[width][height];
            }
            else {
                is = getClass().getResourceAsStream(filePath);
                mapTransparentTileNum = new int[width][height];
            }
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < width && row < height){

                String line = br.readLine();
                while (line.contains("  ")) {
                    line = line.replace("  ", " ");
                }
                while (line.startsWith(" ")){
                    line = line.substring(1);
                }

                String[] numbers = line.split(" ");

                while (col < width){

                    int num = Integer.parseInt(numbers[col]);

                    if (!transparent) {
                        mapCompleteTileNum[col][row] = num;
                    } else {
                        mapTransparentTileNum[col][row] = num;
                    }
                    col++;
                }

                if (col == width){
                    col = 0;
                    row++;
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

package net.rubicon.tile;

import java.io.*;
import java.util.Objects;

public interface IMapManager {

    default int[] getMapDimensions(String filePath){
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

    default void loadMap(int[][][] tileMapNum, String filePath){
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

    default void saveMap(int[][][] tileMapNum, String fileName){

        try { // MAKE THE FILE
            File mapFile = new File("src/res/maps/" + fileName + "-sav.txt");
            if(mapFile.createNewFile()){
                System.out.println("File creation success");
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        // WRITE THE FILE
        try (BufferedWriter mapWriter = new BufferedWriter(new FileWriter("src/res/maps/" + fileName + "-sav.txt"))){

            int layerCount = tileMapNum[0][0].length;
            int maxCol = tileMapNum[0].length;
            int maxRow = tileMapNum.length;

            for (int row = 0; row < maxRow; row++) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int col = 0; col < maxCol; col++) {

                    // TO ADD LAYER INFO
                    for (int layer = 0; layer < layerCount; layer++) {
                        stringBuilder.append(tileMapNum[col][row][layer]);
                        if (layer != layerCount - 1){
                            stringBuilder.append(":");
                        }
                    }

                    if (col != maxCol - 1){
                        stringBuilder.append(" ");
                    }
                }
                mapWriter.write(String.valueOf(stringBuilder));
                mapWriter.newLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

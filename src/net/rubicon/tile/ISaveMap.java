package net.rubicon.tile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public interface ISaveMap {
    default void saveMap(String fileName, int[][][] tileMapNum){

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

package net.rubicon.tile.old;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OldToNewMapMain {

    public static LoadOldMap oldMapData;

    public static void main(String[] args) {
        oldMapData = new LoadOldMap();
        oldMapData.loadMap("map01", 16, 16);

        saveMap("map02-sav");
    }

    private static void saveMap(String fileName){

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

            int[][] completeMap = oldMapData.mapCompleteTileNum;
            int[][] transparentTileNum = oldMapData.mapTransparentTileNum;

            int maxCol = completeMap[0].length;
            int maxRow = completeMap.length;

            for (int row = 0; row < maxRow; row++) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int col = 0; col < maxCol; col++) {

                    stringBuilder.append(completeMap[col][row]).append(":");
                    stringBuilder.append(transparentTileNum[col][row]);
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

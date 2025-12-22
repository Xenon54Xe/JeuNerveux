package net.rubicon.tile;

import net.rubicon.UI.UIImageButton;
import net.rubicon.UI.UITextButton;
import net.rubicon.event.IListener;
import net.rubicon.main.GameCanvas;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MapMaker implements IListener<String> {

    // CLASS VARIABLES
    private boolean active = true;
    private int tileID = 0;
    private final UIImageButton uiButtonTileType;

    private int layer = 0;
    private final UITextButton uiButtonChangeLayer;

    // UTILS
    private final GameCanvas gc;

    public MapMaker(GameCanvas gc) {
        // INIT
        this.gc = gc;

        // EVENT LISTENER
        gc.uiClickEvent.addListener(this);

        // BUTTON TILE TYPE
        int size = (int)(gc.tileSize * 0.8);
        uiButtonTileType = new UIImageButton(gc, gc.tileM.tiles.getTile(tileID).image, Color.BLUE, "Change tile type", "change-tile-type", gc.tileSize, gc.tileSize * 3, gc.tileSize, gc.tileSize, size, size);
        gc.uiM.addUIObject(uiButtonTileType);

        // BUTTON SAVE
        UITextButton uiTextButton = new UITextButton(gc, Color.BLACK, Color.WHITE, "save", "click to save", "save", gc.tileSize, gc.tileSize * 2, gc.tileSize * 3, gc.tileSize);
        gc.uiM.addUIObject(uiTextButton);

        // BUTTON CHANGE LAYER
        uiButtonChangeLayer = new UITextButton(gc, Color.BLACK, Color.WHITE, "layer", "Layer : 0", "layer", gc.tileSize, gc.tileSize * 4, gc.tileSize * 2, gc.tileSize);
        gc.uiM.addUIObject(uiButtonChangeLayer);
    }

    public void update(){
        if (active){
            // Allow to draw tiles
            if (!gc.uiM.isMouseOverUI() && gc.mouseH.leftClickPressed){
                int worldX = (int)((gc.mouseMH.getScreenX() + gc.player.getWorldX() - gc.screenWidth / 2d) / gc.tileSize + 0.5d);
                int worldY = (int)((gc.mouseMH.getScreenY() + gc.player.getWorldY() - gc.screenHeight / 2d) / gc.tileSize + 0.5d);
                if (worldX > gc.maxWorldCol - 1 || worldX < 0 || worldY > gc.maxWorldRow - 1 || worldY < 0){
                    return;
                }
                if (layer == 0) {
                    gc.tileM.mapCompleteTileNum[worldX][worldY] = tileID;
                }
                else {
                    gc.tileM.mapTransparentTileNum[worldX][worldY] = tileID;
                }
            }
        }
    }

    private void saveMap(){
        String fileName = gc.tileM.mapName;

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

            int[][] completeMap = gc.tileM.mapCompleteTileNum;

            for (int row = 0; row < gc.maxWorldRow; row++) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int col = 0; col < gc.maxWorldCol; col++) {

                    stringBuilder.append(completeMap[col][row]);
                    if (col != gc.maxWorldRow - 1){
                        stringBuilder.append(" ");
                    }
                }
                mapWriter.write(String.valueOf(stringBuilder));
                mapWriter.newLine();
            }

        }catch (IOException e){
            e.printStackTrace();
        }

        // WRITE THE FILE
        try (BufferedWriter mapWriter = new BufferedWriter(new FileWriter("src/res/maps/" + fileName + "-sav-t.txt"))){

            int[][] transparentMap = gc.tileM.mapTransparentTileNum;

            for (int row = 0; row < gc.maxWorldRow; row++) {

                StringBuilder stringBuilder = new StringBuilder();
                for (int col = 0; col < gc.maxWorldCol; col++) {

                    stringBuilder.append(transparentMap[col][row]);
                    if (col != gc.maxWorldRow - 1){
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

    @Override
    public void onTrigger(String payload) {

        // CHANGE TILE TYPE EVENT
        if (payload.equals("change-tile-type-left") || payload.equals("change-tile-type-right")) {
            if (payload.equals("change-tile-type-left")) {
                if (layer == 0) {
                    tileID = gc.tileM.tiles.getNextComplete((tileID + 1) % gc.tileM.tiles.size()).getID();
                }
                else {
                    tileID = gc.tileM.tiles.getNextTransparent((tileID + 1) % gc.tileM.tiles.size()).getID();
                }
            }
            if (payload.equals("change-tile-type-right")) {
                tileID --;
                if (tileID < 0){
                    tileID += gc.tileM.tiles.size();
                }

                if (layer == 0) {
                    tileID = gc.tileM.tiles.getPreviousComplete(tileID).getID();
                }
                else {
                    tileID = gc.tileM.tiles.getPreviousTransparent(tileID).getID();
                }
            }

            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).image);
        }

        // SAVE MAP EVENT
        if (payload.equals("save-left")){
            saveMap();
        }

        // CHANGE LAYER
        if (payload.equals("layer-left")){
            layer = (layer + 1) % 2;
            uiButtonChangeLayer.setText("Layer : " + layer);
            if (layer == 0){
                tileID = gc.tileM.tiles.getNextComplete(tileID).getID();
            }
            else {
                tileID = gc.tileM.tiles.getNextTransparent(tileID).getID();
            }
            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).image);
        }
    }
}

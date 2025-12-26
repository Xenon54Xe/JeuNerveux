package net.rubicon.tile;

import net.rubicon.event.IEventComponent;
import net.rubicon.ui.UIImageButton;
import net.rubicon.ui.UIObject;
import net.rubicon.ui.UITextButton;
import net.rubicon.event.IListener;
import net.rubicon.event.ComponentUIClick;
import net.rubicon.main.GameCanvas;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MapMakerManager implements IListener {

    // UTILS
    private final GameCanvas gc;

    // CLASS VARIABLES
    private boolean active;

    // UI NAMES
    public static final String ACTIVATE_MAPMAKING = "activate";
    public static final String CHANGE_TILE_TYPE = "tile-type";
    public static final String CHANGE_LAYER = "layer";
    public static final String SAVE_MAP = "save";
    
    // UI
    private int tileID = 1;
    private final UIImageButton uiButtonTileType;
    private int layer = 0;
    private final UITextButton uiButtonChangeLayer;

    private ArrayList<UIObject> objectsToHide = new ArrayList<>();

    public MapMakerManager(GameCanvas gc) {
        // INIT
        this.gc = gc;

        // EVENT LISTENER
        gc.eventUIClick.addListener(this);

        // BUTTON TILE TYPE
        int size = (int)(gc.tileSize * 0.8);
        uiButtonTileType = new UIImageButton(gc, gc.tileM.tiles.getTile(tileID).getImage(), Color.BLUE, CHANGE_TILE_TYPE, gc.tileSize, gc.tileSize * 3, gc.tileSize, gc.tileSize, size, size);
        gc.uiM.addUIObject(uiButtonTileType);

        // BUTTON SAVE
        UITextButton uiTextButtonSave = new UITextButton(gc, Color.BLACK, Color.WHITE, SAVE_MAP, "click to save", gc.tileSize, gc.tileSize * 2, 10, 10);
        gc.uiM.addUIObject(uiTextButtonSave);

        // BUTTON CHANGE LAYER
        uiButtonChangeLayer = new UITextButton(gc, Color.BLACK, Color.WHITE, CHANGE_LAYER, "Layer : 0", gc.tileSize, gc.tileSize * 4, 10, 10);
        gc.uiM.addUIObject(uiButtonChangeLayer);

        // ACTIVATE
        UITextButton uiTextButtonActivate = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAPMAKING, "Activate map making", gc.tileSize * 10, gc.tileSize, 10, 10);
        gc.uiM.addUIObject(uiTextButtonActivate);

        // REGISTERING OBJECTS TO HIDE
        objectsToHide.add(uiButtonTileType);
        objectsToHide.add(uiTextButtonSave);
        objectsToHide.add(uiButtonChangeLayer);

        // MAKING THEM HIDDEN OR NOT
        setActive(false);
    }

    public void update(){
        if (active){
            // Allow to draw tiles
            if (!gc.uiM.isMouseOverUI() && gc.mouseH.leftClickPressed){
                int worldX = (gc.mouseMH.getScreenX() + gc.tracked.getCameraWorldX()) / gc.tileSize;
                int worldY = (gc.mouseMH.getScreenY() + gc.tracked.getCameraWorldY()) / gc.tileSize;

                if (worldX > gc.tileM.getMaxWorldCol() - 1 || worldX < 0 || worldY > gc.tileM.getMaxWorldRow() - 1 || worldY < 0){
                    return;
                }
                gc.tileM.tileMapNum[worldX][worldY][layer] = tileID;
            }
        }
    }

    private void saveMap(int[][][] tileMapNum, String fileName){

        try { // MAKE THE FILE
            File mapFile = new File("src/res/maps/" + fileName);
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

    public void saveMap() {
        saveMap(gc.tileM.tileMapNum, gc.tileM.getMapName() + "-sav.txt");
    }

    private void setActive(boolean active){
        this.active = active;

        for (UIObject object : objectsToHide){
            object.setShow(active);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String buttonClicked)) {

            String message = uiObject.getName() + buttonClicked;

            // CHANGE TILE TYPE EVENT
            if (message.equals(CHANGE_TILE_TYPE + ComponentUIClick.LEFT_BUTTON) || message.equals(CHANGE_TILE_TYPE + ComponentUIClick.RIGHT_BUTTON)) {
                if (message.equals(CHANGE_TILE_TYPE + ComponentUIClick.LEFT_BUTTON)) {
                    tileID = gc.tileM.tiles.getNextLayerTile((tileID + 1) % gc.tileM.tiles.size(), layer).getID();
                }
                if (message.equals(CHANGE_TILE_TYPE + ComponentUIClick.RIGHT_BUTTON)) {
                    tileID--;
                    if (tileID < 0) {
                        tileID += gc.tileM.tiles.size();
                    }
                    tileID = gc.tileM.tiles.getPreviousLayerTile(tileID, layer).getID();
                }

                uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
            }

            // SAVE MAP EVENT
            if (message.equals(SAVE_MAP + ComponentUIClick.LEFT_BUTTON)) {
                saveMap();
            }

            // CHANGE LAYER
            if (message.equals(CHANGE_LAYER + ComponentUIClick.LEFT_BUTTON)) {
                layer = (layer + 1) % gc.tileM.getLayerCount();
                uiButtonChangeLayer.setText("Layer : " + layer);
                tileID = gc.tileM.tiles.getNextLayerTile(tileID, layer).getID();

                uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
            }

            // ACTIVATE MAP MAKER
            if (message.equals(ACTIVATE_MAPMAKING + ComponentUIClick.LEFT_BUTTON)) {
                setActive(!active);
            }
        }
    }
}

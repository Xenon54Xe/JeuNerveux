package net.rubicon.tile;

import net.rubicon.UI.UIImageButton;
import net.rubicon.UI.UIObject;
import net.rubicon.UI.UITextButton;
import net.rubicon.event.IListener;
import net.rubicon.event.ECUIClick;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class MapMaker implements IListener<ECUIClick>, IMapManager {

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

    private final UIObject[] objectsToHide;


    public MapMaker(GameCanvas gc) {
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
        objectsToHide = new UIObject[3];
        objectsToHide[0] = uiButtonTileType;
        objectsToHide[1] = uiTextButtonSave;
        objectsToHide[2] = uiButtonChangeLayer;

        // MAKING THEM HIDDEN OR NOT
        setActive(false);
    }

    public void update(){
        if (active){
            // Allow to draw tiles
            if (!gc.uiM.isMouseOverUI() && gc.mouseH.leftClickPressed){
                int worldX = (gc.mouseMH.getScreenX() + gc.tracked.getCameraWorldX()) / gc.tileSize;
                int worldY = (gc.mouseMH.getScreenY() + gc.tracked.getCameraWorldY()) / gc.tileSize;

                if (worldX > gc.maxWorldCol - 1 || worldX < 0 || worldY > gc.maxWorldRow - 1 || worldY < 0){
                    return;
                }
                gc.tileM.tileMapNum[worldX][worldY][layer] = tileID;
            }
        }
    }

    public void saveMap() {
        IMapManager.super.saveMap(gc.tileM.tileMapNum, gc.tileM.mapName);
    }

    private void setActive(boolean active){
        this.active = active;

        for (UIObject object : objectsToHide){
            object.setShow(active);
        }
    }

    @Override
    public void onTrigger(ECUIClick eventComponent) {

        UIObject uiObject = eventComponent.uiObject();
        String buttonClicked = eventComponent.buttonClicked();
        
        String message = uiObject.getName() + buttonClicked;
        
        // CHANGE TILE TYPE EVENT
        if (message.equals(CHANGE_TILE_TYPE + ECUIClick.LEFT_BUTTON) || message.equals(CHANGE_TILE_TYPE + ECUIClick.RIGHT_BUTTON)) {
            if (message.equals(CHANGE_TILE_TYPE + ECUIClick.LEFT_BUTTON)) {
                tileID = gc.tileM.tiles.getNextLayerTile((tileID + 1) % gc.tileM.tiles.size(), layer).getID();
            }
            if (message.equals(CHANGE_TILE_TYPE + ECUIClick.RIGHT_BUTTON)) {
                tileID --;
                if (tileID < 0){
                    tileID += gc.tileM.tiles.size();
                }
                tileID = gc.tileM.tiles.getPreviousLayerTile(tileID, layer).getID();
            }

            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
        }

        // SAVE MAP EVENT
        if (message.equals(SAVE_MAP + ECUIClick.LEFT_BUTTON)){
            saveMap();
        }

        // CHANGE LAYER
        if (message.equals(CHANGE_LAYER + ECUIClick.LEFT_BUTTON)){
            layer = (layer + 1) % gc.layerCount;
            uiButtonChangeLayer.setText("Layer : " + layer);
            tileID = gc.tileM.tiles.getNextLayerTile(tileID, layer).getID();

            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
        }

        // ACTIVATE MAP MAKER
        if (message.equals(ACTIVATE_MAPMAKING + ECUIClick.LEFT_BUTTON)){
            setActive(!active);
        }
    }
}

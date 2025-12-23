package net.rubicon.tile;

import net.rubicon.UI.UIImageButton;
import net.rubicon.UI.UIObject;
import net.rubicon.UI.UITextButton;
import net.rubicon.event.IListener;
import net.rubicon.main.GameCanvas;

import java.awt.*;

public class MapMaker implements IListener<String>, IMapManager {

    // UTILS
    private final GameCanvas gc;

    // CLASS VARIABLES
    private boolean active;

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
        gc.uiClickEvent.addListener(this);

        // BUTTON TILE TYPE
        int size = (int)(gc.tileSize * 0.8);
        uiButtonTileType = new UIImageButton(gc, gc.tileM.tiles.getTile(tileID).getImage(), Color.BLUE, "change-tile-type", gc.tileSize, gc.tileSize * 3, gc.tileSize, gc.tileSize, size, size);
        gc.uiM.addUIObject(uiButtonTileType);

        // BUTTON SAVE
        UITextButton uiTextButtonSave = new UITextButton(gc, Color.BLACK, Color.WHITE, "save", "click to save", gc.tileSize, gc.tileSize * 2, 10, 10);
        gc.uiM.addUIObject(uiTextButtonSave);

        // BUTTON CHANGE LAYER
        uiButtonChangeLayer = new UITextButton(gc, Color.BLACK, Color.WHITE, "layer", "Layer : 0", gc.tileSize, gc.tileSize * 4, 10, 10);
        gc.uiM.addUIObject(uiButtonChangeLayer);

        // ACTIVATE
        UITextButton uiTextButtonActivate = new UITextButton(gc, Color.BLACK, Color.WHITE, "activate", "Activate map making", gc.tileSize * 10, gc.tileSize, 10, 10);
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
                int worldX = (int)((gc.mouseMH.getScreenX() + gc.player.getWorldX() - gc.screenWidth / 2d) / gc.tileSize + 0.5d);
                int worldY = (int)((gc.mouseMH.getScreenY() + gc.player.getWorldY() - gc.screenHeight / 2d) / gc.tileSize + 0.5d);
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
    public void onTrigger(String payload) {

        // CHANGE TILE TYPE EVENT
        if (payload.equals("change-tile-type-left") || payload.equals("change-tile-type-right")) {
            if (payload.equals("change-tile-type-left")) {
                tileID = gc.tileM.tiles.getNextLayerTile((tileID + 1) % gc.tileM.tiles.size(), layer).getID();
            }
            if (payload.equals("change-tile-type-right")) {
                tileID --;
                if (tileID < 0){
                    tileID += gc.tileM.tiles.size();
                }
                tileID = gc.tileM.tiles.getPreviousLayerTile(tileID, layer).getID();
            }

            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
        }

        // SAVE MAP EVENT
        if (payload.equals("save-left")){
            saveMap();
        }

        // CHANGE LAYER
        if (payload.equals("layer-left")){
            layer = (layer + 1) % gc.layerCount;
            uiButtonChangeLayer.setText("Layer : " + layer);
            tileID = gc.tileM.tiles.getNextLayerTile(tileID, layer).getID();

            uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
        }

        // ACTIVATE MAP MAKER
        if (payload.equals("activate-left")){
            setActive(!active);
        }
    }
}

package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.*;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.*;
import com.example.app.ui.frame.UIFrame;
import com.example.app.utils.FileUtils;

import java.awt.*;

public class MapDrawerManager implements IListener {

    // UTILS
    private final GameCanvas gc;

    // UI NAMES
    public static final String ACTIVATE_MAPMAKING = "activate-map-making";
    public static final String CHANGE_TILE_TYPE = "change-tile-type";
    public static final String CHANGE_LAYER = "change-nbLayer";
    public static final String SAVE_MAP = "save-map";

    // UI MENU
    private final UIFrame uiFrame;

    // UI
    private int tileID = 1;
    private final UIImageButton uiButtonTileType;
    private int layer = 0;
    private final UITextButton uiButtonChangeLayer;

    public MapDrawerManager(GameCanvas gc) {
        // INIT
        this.gc = gc;

        // EVENT LISTENER
        register(gc.eventUIClick);

//        // ACTIVATE BUTTON (always shown)
//        UITextButton uiTextButtonActivate = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAPMAKING, "Activate map making", gc.tileSize * 10, gc.tileSize, 10, 10);
//        gc.uiM.addUIObject(uiTextButtonActivate);
        // UI
        uiFrame = new UIFrame(gc, "Draw the map",
                UIObject.DRAW_TOP_LEFT_CORNER, 1, 3);
        uiFrame.setDrawStepBetweenEdges(10, 10);
        uiFrame.expand();

        // BUTTON TILE TYPE
        int size = (int)(gc.tileSize * 0.8);
        uiButtonTileType = new UIImageButton(gc, gc.tileM.tiles.getTile(tileID).getImage(), Color.BLUE, CHANGE_TILE_TYPE, gc.tileSize, gc.tileSize * 3, gc.tileSize, gc.tileSize, size, size);
        // BUTTON SAVE
        UITextButton uiTextButtonSave = new UITextButton(gc, Color.BLACK, Color.WHITE, SAVE_MAP, "click to save", gc.tileSize, gc.tileSize * 2, 10, 10);
        // BUTTON CHANGE LAYER
        uiButtonChangeLayer = new UITextButton(gc, Color.BLACK, Color.WHITE, CHANGE_LAYER, "Layer : 0", gc.tileSize, gc.tileSize * 4, 10, 10);
        // Register
        uiFrame.addUIObject(uiButtonTileType, 0, 1);
        uiFrame.addUIObject(uiTextButtonSave, 0, 0);
        uiFrame.addUIObject(uiButtonChangeLayer, 0, 2);

        // MAKING THEM HIDDEN OR NOT
        setShow(false);
    }

    public void update(){
        if (uiFrame.isShow()){
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

    public void saveMap() {
        FileUtils.saveMap(gc.tileM.tileMapNum, gc.tileM.getMapName() + "-sav.txt");
        gc.loadMapM.reloadAvailableMaps();
    }

    public void setShow(boolean active){
        uiFrame.setShow(active);
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

//            // ACTIVATE MAP MAKER
//            if (message.equals(ACTIVATE_MAPMAKING + ComponentUIClick.LEFT_BUTTON)) {
//                setActive(!uiMenu.isActive());
//            }
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

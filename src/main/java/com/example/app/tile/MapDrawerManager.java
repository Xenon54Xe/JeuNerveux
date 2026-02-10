package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.*;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.*;
import com.example.app.ui.frame.UIFrame;
import com.example.app.utils.FileUtils;

import java.awt.*;

public class MapDrawerManager implements Listener {

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

    // UPDATE MAP
    private final int updateDelay = 180;
    private int updateCount = 0;

    public MapDrawerManager(GameCanvas gc) {
        // INIT
        this.gc = gc;

        // EVENT LISTENER
        register();

//        // ACTIVATE BUTTON (always shown)
//        UITextButton uiTextButtonActivate = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAPMAKING, "Activate map making", gc.tileSize * 10, gc.tileSize, 10, 10);
//        gc.uiM.addUIObject(uiTextButtonActivate);
        // UI
        uiFrame = new UIFrame(gc, "Draw the map",
                UIObject.DRAW_TOP_LEFT, 1, 3);
        uiFrame.setDrawStepBetweenEdges(10, 10);
        uiFrame.expand();

        // BUTTON TILE TYPE
        int size = (int)(gc.TILE_SIZE * 0.8);
        uiButtonTileType = new UIImageButton(gc, gc.tileM.tiles.getTile(tileID).getImage(), Color.BLUE, CHANGE_TILE_TYPE, gc.TILE_SIZE, gc.TILE_SIZE * 3, gc.TILE_SIZE, gc.TILE_SIZE, size, size);
        // BUTTON SAVE
        UITextButton uiTextButtonSave = new UITextButton(gc, Color.BLACK, Color.WHITE, SAVE_MAP, "click to save", gc.TILE_SIZE, gc.TILE_SIZE * 2, 10, 10);
        // BUTTON CHANGE LAYER
        uiButtonChangeLayer = new UITextButton(gc, Color.BLACK, Color.WHITE, CHANGE_LAYER, "Layer : 0", gc.TILE_SIZE, gc.TILE_SIZE * 4, 10, 10);
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
                int worldX = (gc.mouseMH.getScreenX() + gc.getTracked().getCameraWorldX()) / gc.TILE_SIZE;
                int worldY = (gc.mouseMH.getScreenY() + gc.getTracked().getCameraWorldY()) / gc.TILE_SIZE;

                if (worldX > gc.tileM.getMaxWorldCol() - 1 || worldX < 0 || worldY > gc.tileM.getMaxWorldRow() - 1 || worldY < 0){
                    return;
                }
                gc.tileM.tileMap.setTileNum(tileID, worldX, worldY, layer);
            }

            if (updateCount <= 0){
                updateCount = updateDelay;
                //gc.uiM.uiMap.initMap(gc.tileM.tileMap);
            }
            updateCount--;
        }
    }

    public void saveMap() {
        String name = gc.tileM.getMapName();
        if (name.length() >= 5 && name.substring(name.length() - 5, name.length() - 1).equals("-sav")){
            int nb = Integer.parseInt(name.substring(name.length() - 1));
            name = name.substring(0, name.length() - 1) + (nb + 1);
        }
        else {
            name = gc.tileM.getMapName() + "-sav0";
        }
        FileUtils.saveMap(gc.tileM.tileMap, name);
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
                    tileID = gc.tileM.tiles.getNextTile((tileID + 1) % gc.tileM.tiles.size()).getID();
                }
                if (message.equals(CHANGE_TILE_TYPE + ComponentUIClick.RIGHT_BUTTON)) {
                    tileID--;
                    if (tileID < 0) {
                        tileID += gc.tileM.tiles.size();
                    }
                    tileID = gc.tileM.tiles.getPreviousTile(tileID).getID();
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
                tileID = gc.tileM.tiles.getNextTile(tileID).getID();

                uiButtonTileType.setImage(gc.tileM.tiles.getTile(tileID).getImage());
            }

//            // ACTIVATE MAP MAKER
//            if (message.equals(ACTIVATE_MAPMAKING + ComponentUIClick.LEFT_BUTTON)) {
//                setActive(!uiMenu.isActive());
//            }
        }
    }

    @Override
    public void register() {
        gc.eventUIClick.addListener(this);
    }
}

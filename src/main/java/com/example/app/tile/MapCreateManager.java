package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.SceneryManager;
import com.example.app.event.*;
import com.example.app.event.component.ComponentCreateMap;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.*;
import com.example.app.ui.frame.UIFrame;
import com.example.app.utils.FileUtils;

import java.awt.*;

public class MapCreateManager implements Listener {

    final GameCanvas gc;

    public static String ACTIVATE_MAP_CREATING = "activate-map-creating";
    public static String CREATE_MAP = "create-map";

    // CLASS VARIABLES
    UIFrame uiFrameCreateEmptyMap;

    private final WritableBox mapNameWB;
    private final WritableBox mapWidthWB;
    private final WritableBox mapHeightWB;
    private final WritableBox mapLayerWB;

    public MapCreateManager(GameCanvas gc){
        this.gc = gc;

        // NEW MAP
        uiFrameCreateEmptyMap = new UIFrame(gc, "Create empty map", UIObject.DRAW_CENTER,
                1, 6);
        uiFrameCreateEmptyMap.setDrawEvenly();
        uiFrameCreateEmptyMap.expand();
        // TITLE
        UIText newMapTitle = new UIText(Color.white, "New Map", gc.SCREEN_WIDTH / 2, gc.TILE_SIZE);
        newMapTitle.setText("New Map");
        // MAP NAME
        mapNameWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Name",
                gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2 - gc.TILE_SIZE * 2,
                gc.TILE_SIZE * 2, 10, 10, 10);
        // MAP WIDTH (TILE)
        mapWidthWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Width",
                gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2 - gc.TILE_SIZE,
                gc.TILE_SIZE * 2, 10, 10, 10);
        // MAP HEIGHT (TILE)
        mapHeightWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Height",
                gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2,
                gc.TILE_SIZE * 2, 10, 10, 10);
        // MAP LAYER (TILE)
        mapLayerWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Layer",
                gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2 + gc.TILE_SIZE,
                gc.TILE_SIZE * 3, 10, 10, 10);
        // CREATE MAP
        UITextButton createMapButton = new UITextButton(gc, Color.BLACK, Color.WHITE, CREATE_MAP, "Create",
                gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2 + gc.TILE_SIZE * 2, 10, 10);
        // Register create empty map
        uiFrameCreateEmptyMap.addUIObject(newMapTitle, 0, 0);
        uiFrameCreateEmptyMap.addUIObject(mapNameWB, 0, 1);
        uiFrameCreateEmptyMap.addUIObject(mapWidthWB, 0, 2);
        uiFrameCreateEmptyMap.addUIObject(mapHeightWB, 0, 3);
        uiFrameCreateEmptyMap.addUIObject(mapLayerWB, 0, 4);
        uiFrameCreateEmptyMap.addUIObject(createMapButton, 0, 5);

        register();
    }

    public void setShow(boolean show) {
        uiFrameCreateEmptyMap.setShow(show);
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String buttonClicked)) {

            String payload = uiObject.getName();

            if(payload.equals(CREATE_MAP) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)){
                String name = mapNameWB.getText();
                String nbColS = mapWidthWB.getText();
                String nbRowS = mapHeightWB.getText();
                String nbLayerS = mapLayerWB.getText();
                int nbCol = Integer.parseInt(nbColS);
                int nbRow = Integer.parseInt(nbRowS);
                int nbLayer = Integer.parseInt(nbLayerS);

                // CREATE NEW MAP
                TileMap tileMap = getTileMap(nbCol, nbRow, nbLayer);
                FileUtils.saveMap(tileMap, name);

                // LOAD THE NEW MAP
                gc.tileM.loadMap(name);

                // Scenery
                gc.sceneryM.safeChangeScenery(SceneryManager.CLEAN_SCENERY);

                // EVENT
                gc.eventCreateMap.trigger(new ComponentCreateMap(name, nbCol, nbRow, nbLayer));
            }
        }
    }

    private static TileMap getTileMap(int nbCol, int nbRow, int nbLayer) {
        TileMap tileMap = new TileMap(nbCol, nbRow, nbLayer);
        for (int row = 0; row < nbRow; row++) {

            for (int col = 0; col < nbCol; col++) {

                for (int layer = 0; layer < nbLayer; layer++) {

                    if (layer == 0){
                        tileMap.setTileNum(1, col, row, layer);
                    }
                    else {
                        tileMap.setTileNum(0, col, row, layer);
                    }
                }
            }
        }
        return tileMap;
    }

    @Override
    public void register() {
        gc.eventUIClick.addListener(this);
    }
}

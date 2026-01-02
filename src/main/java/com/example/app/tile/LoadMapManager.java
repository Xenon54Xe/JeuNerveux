package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentCreateMap;
import com.example.app.event.ComponentUIClick;
import com.example.app.event.IEventComponent;
import com.example.app.event.IListener;
import com.example.app.ui.*;
import com.example.app.utils.FileUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class LoadMapManager implements IListener {

    final GameCanvas gc;

    // PAYLOADS
    public static String ACTIVATE_MAP_LOADING = "activate-map-loading";
    public static String CHANGE_MAP = "change-map";
    public static String VALIDATE = "validate-map";
    public static String CREATE_MAP = "create-map";

    private String[] foundMapsName;
    private String selectedMapName;
    private int index = 0;

    // UI MENU
    UIFrame uiFrameLoadMap;
    UIFrame uiFrameCreateEmptyMap;
    // CREATE MAP UI
    private final UITextButton changeMapButton;
    private final WritableBox mapNameWB;
    private final WritableBox mapWidthWB;
    private final WritableBox mapHeightWB;
    private final WritableBox mapLayerWB;

    public LoadMapManager(GameCanvas gc){
        this.gc = gc;

//        // MAIN UI
//        UITextButton activateLoadMap = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAP_LOADING, "Activate map loading", gc.tileSize * 10, gc.tileSize * 2, 10, 10);
//        gc.uiM.addUIObject(activateLoadMap);
        // UI MENU

        // LOAD MAP
        uiFrameLoadMap = new UIFrame(gc, "Load Menu");
        uiFrameLoadMap.setShape(1, 2);
        uiFrameLoadMap.setDrawEvenly();
        uiFrameLoadMap.setScreenX(gc.tileSize * 3);
        uiFrameLoadMap.setScreenY(0);
        uiFrameLoadMap.setWidth(gc.tileSize * 2);
        uiFrameLoadMap.setHeight(gc.tileSize * 3);
        // UI
        changeMapButton = new UITextButton(gc, Color.WHITE, Color.BLACK, CHANGE_MAP, "Current map :", gc.tileSize, (int)(gc.screenHeight * 0.8), 10, 10);
        UITextButton validateButton = new UITextButton(gc, Color.WHITE, Color.BLACK, VALIDATE, "Load", gc.tileSize, (int)(gc.screenHeight * 0.8) + gc.tileSize, 10, 10);
        // Register load map
        uiFrameLoadMap.addUIObject(changeMapButton, 0, 0);
        uiFrameLoadMap.addUIObject(validateButton, 0, 1);


        // NEW MAP
        uiFrameCreateEmptyMap = new UIFrame(gc, "Create empty map");
        uiFrameCreateEmptyMap.setShape(1, 6);
        uiFrameCreateEmptyMap.setDrawEvenly();
        uiFrameCreateEmptyMap.expand();
        // TITLE
        UIText newMapTitle = new UIText(Color.white, "New Map", gc.screenWidth / 2, gc.tileSize);
        newMapTitle.setText("New Map");
        newMapTitle.setDrawCentered(true);
        // MAP NAME
        mapNameWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Name",
                gc.screenWidth / 2, gc.screenHeight / 2 - gc.tileSize * 2,
                gc.tileSize * 2, 10, 10, 10);
        mapNameWB.setDrawCentered(true);
        // MAP WIDTH (TILE)
        mapWidthWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Width",
                gc.screenWidth / 2, gc.screenHeight / 2 - gc.tileSize,
                gc.tileSize * 2, 10, 10, 10);
        mapWidthWB.setDrawCentered(true);
        // MAP HEIGHT (TILE)
        mapHeightWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Height",
                gc.screenWidth / 2, gc.screenHeight / 2,
                gc.tileSize * 2, 10, 10, 10);
        mapHeightWB.setDrawCentered(true);
        // MAP LAYER (TILE)
        mapLayerWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Layer",
                gc.screenWidth / 2, gc.screenHeight / 2 + gc.tileSize,
                gc.tileSize * 3, 10, 10, 10);
        mapLayerWB.setDrawCentered(true);
        // CREATE MAP
        UITextButton createMapButton = new UITextButton(gc, Color.BLACK, Color.WHITE, CREATE_MAP, "Create",
                gc.screenWidth / 2, gc.screenHeight / 2 + gc.tileSize * 2, 10, 10);
        createMapButton.setDrawCentered(true);
        // Register create empty map
        uiFrameCreateEmptyMap.addUIObject(newMapTitle, 0, 0);
        uiFrameCreateEmptyMap.addUIObject(mapNameWB, 0, 1);
        uiFrameCreateEmptyMap.addUIObject(mapWidthWB, 0, 2);
        uiFrameCreateEmptyMap.addUIObject(mapHeightWB, 0, 3);
        uiFrameCreateEmptyMap.addUIObject(mapLayerWB, 0, 4);
        uiFrameCreateEmptyMap.addUIObject(createMapButton, 0, 5);

        // EVENT
        gc.eventUIClick.addListener(this);

        // END
        setActive(false);
    }

    public void reloadAvailableMaps(){
        try {
            foundMapsName = FileUtils.listAllResources("maps").toArray(new String[0]);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void setActive(boolean active) {
        uiFrameLoadMap.setShow(active);

        if (active) {
            // get every available map
            reloadAvailableMaps();
            selectedMapName = foundMapsName[index];

            changeMapButton.setText("Current map : " + selectedMapName);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String buttonClicked)) {

            String payload = uiObject.getName();

            if (payload.equals(CHANGE_MAP) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {

                // CHANGE SELECTED MAP
                index = (index + 1) % foundMapsName.length;
                selectedMapName = foundMapsName[index];

                changeMapButton.setText("Current map : " + selectedMapName);
            }
            if (payload.equals(VALIDATE) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {

                // LOAD MAP
                gc.tileM.setMapName(selectedMapName);
                gc.tileM.loadMap();
            }
//            if (payload.equals(ACTIVATE_MAP_LOADING) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)){
//                setActive(!uiMenu.isActive());
//            }

            if(payload.equals(CREATE_MAP) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)){
                String name = mapNameWB.getText();
                String nbColS = mapWidthWB.getText();
                String nbRowS = mapHeightWB.getText();
                String nbLayerS = mapLayerWB.getText();
                int nbCol = Integer.parseInt(nbColS);
                int nbRow = Integer.parseInt(nbRowS);
                int nbLayer = Integer.parseInt(nbLayerS);

                // CREATE NEW MAP
                int[][][] tileMapNum = new int[nbCol][nbRow][nbLayer];
                for (int row = 0; row < nbRow; row++) {

                    for (int col = 0; col < nbCol; col++) {

                        for (int layer = 0; layer < nbLayer; layer++) {

                            if (layer == 0){
                                tileMapNum[col][row][layer] = 1;
                            }
                            else {
                                tileMapNum[col][row][layer] = 0;
                            }
                        }
                    }
                }
                FileUtils.saveMap(tileMapNum, name);
                reloadAvailableMaps();

                // LOAD THE NEW MAP
                gc.tileM.setMapName(name);
                gc.tileM.loadMap();

                // EVENT
                gc.eventCreateMap.trigger(new ComponentCreateMap(name, nbCol, nbRow, nbLayer));
            }
        }
    }
}

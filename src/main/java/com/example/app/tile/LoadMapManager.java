package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentCreateMap;
import com.example.app.event.ComponentUIClick;
import com.example.app.event.IEventComponent;
import com.example.app.event.IListener;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;
import com.example.app.ui.UITextButton;
import com.example.app.ui.WritableBox;
import com.example.app.utils.FileUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class LoadMapManager implements IListener {

    final GameCanvas gc;

    // PAYLOADS
    public static String CHANGE_MAP = "change-map";
    public static String VALIDATE = "validate-map";
    public static String ACTIVATE_MAP_LOADING = "activate-map-loading";
    public static String CREATE_MAP = "create-map";

    // CLASS VARIABLES
    private boolean active;
    private final ArrayList<UIObject> whenActiveObjects = new ArrayList<>();

    private String[] foundMaps;
    private String selectedMap;
    private int index = 0;

    // MAIN UI
    UITextButton changeMapButton;

    // CREATE MAP UI
    WritableBox mapNameWB;
    WritableBox mapWidthWB;
    WritableBox mapHeightWB;
    WritableBox mapLayerWB;

    public LoadMapManager(GameCanvas gc){
        this.gc = gc;

        // MAIN UI
        UITextButton activateLoadMap = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAP_LOADING, "Activate map loading", gc.tileSize * 10, gc.tileSize * 2, 10, 10);
        gc.uiM.addUIObject(activateLoadMap);

        // LOAD MAP
        changeMapButton = new UITextButton(gc, Color.WHITE, Color.BLACK, CHANGE_MAP, "Current map :", gc.tileSize, (int)(gc.screenHeight * 0.8), 10, 10);
        gc.uiM.addUIObject(changeMapButton);
        UITextButton validateButton = new UITextButton(gc, Color.WHITE, Color.BLACK, VALIDATE, "Load", gc.tileSize, (int)(gc.screenHeight * 0.8) + gc.tileSize, 10, 10);
        gc.uiM.addUIObject(validateButton);
        whenActiveObjects.add(changeMapButton);
        whenActiveObjects.add(validateButton);

        // NEW MAP
        // TITLE
        UIText newMapTitle = new UIText(Color.white, "New Map", gc.screenWidth / 2, gc.tileSize);
        newMapTitle.setText("New Map");
        newMapTitle.setDrawCentered(true);
        gc.uiM.addUIObject(newMapTitle);
        whenActiveObjects.add(newMapTitle);
        // MAP NAME
        mapNameWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Name",
                gc.screenWidth / 2, gc.screenHeight / 2 - gc.tileSize * 2,
                gc.tileSize * 2, 10, 10, 10);
        mapNameWB.setDrawCentered(true);
        gc.uiM.addUIObject(mapNameWB);
        whenActiveObjects.add(mapNameWB);
        // MAP WIDTH (TILE)
        mapWidthWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Width",
                gc.screenWidth / 2, gc.screenHeight / 2 - gc.tileSize,
                gc.tileSize * 2, 10, 10, 10);
        mapWidthWB.setDrawCentered(true);
        gc.uiM.addUIObject(mapWidthWB);
        whenActiveObjects.add(mapWidthWB);
        // MAP HEIGHT (TILE)
        mapHeightWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Height",
                gc.screenWidth / 2, gc.screenHeight / 2,
                gc.tileSize * 2, 10, 10, 10);
        mapHeightWB.setDrawCentered(true);
        gc.uiM.addUIObject(mapHeightWB);
        whenActiveObjects.add(mapHeightWB);
        // MAP LAYER (TILE)
        mapLayerWB = new WritableBox(gc, Color.GRAY, Color.BLACK, "Layer",
                gc.screenWidth / 2, gc.screenHeight / 2 + gc.tileSize,
                gc.tileSize * 3, 10, 10, 10);
        mapLayerWB.setDrawCentered(true);
        gc.uiM.addUIObject(mapLayerWB);
        whenActiveObjects.add(mapLayerWB);
        // CREATE MAP
        UITextButton createMapButton = new UITextButton(gc, Color.BLACK, Color.WHITE, CREATE_MAP, "Create",
                gc.screenWidth / 2, gc.screenHeight / 2 + gc.tileSize * 2, 10, 10);
        createMapButton.setDrawCentered(true);
        gc.uiM.addUIObject(createMapButton);
        whenActiveObjects.add(createMapButton);

        // EVENT
        gc.eventUIClick.addListener(this);

        // END
        setActive(false);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;

        for (UIObject object : whenActiveObjects){
            object.setShow(active);
        }

        if (active) {
            // get every available map
            try {
                foundMaps = FileUtils.listResources("maps").toArray(new String[0]);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
            selectedMap = foundMaps[index];

            changeMapButton.setText("Current map : " + selectedMap);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String buttonClicked)) {

            String payload = uiObject.getName();

            if (payload.equals(CHANGE_MAP) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {

                // CHANGE SELECTED MAP
                index = (index + 1) % foundMaps.length;
                selectedMap = foundMaps[index];

                changeMapButton.setText("Current map : " + selectedMap);
            }
            if (payload.equals(VALIDATE) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {

                // LOAD MAP
                gc.tileM.setMapName(selectedMap);
                gc.tileM.loadMap();
            }
            if (payload.equals(ACTIVATE_MAP_LOADING) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)){
                setActive(!active);
            }

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

                // LOAD THE NEW MAP
                gc.tileM.setMapName(name);
                gc.tileM.setTileMapNum(tileMapNum);

                // EVENT
                gc.eventCreateMap.trigger(new ComponentCreateMap(name, nbCol, nbRow, nbLayer));
            }
        }
    }
}

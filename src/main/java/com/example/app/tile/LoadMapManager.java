package com.example.app.tile;

import com.example.app.GameCanvas;
import com.example.app.SceneryManager;
import com.example.app.event.*;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.*;
import com.example.app.ui.frame.UIFrame;
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

    private String[] foundMapsName;
    private String selectedMapName;
    private int index = 0;

    // UI MENU
    UIFrame uiFrameLoadMap;
    // CREATE MAP UI
    private final UITextButton changeMapButton;

    public LoadMapManager(GameCanvas gc){
        this.gc = gc;

//        // MAIN UI
//        UITextButton activateLoadMap = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAP_LOADING, "Activate map loading", gc.tileSize * 10, gc.tileSize * 2, 10, 10);
//        gc.uiM.addUIObject(activateLoadMap);
        // UI MENU

        // LOAD MAP
        uiFrameLoadMap = new UIFrame(gc, "Load Menu", UIObject.DRAW_TOP_LEFT_CORNER,
                1, 2);
        uiFrameLoadMap.setDrawStepBetweenEdges(10, 10);
        uiFrameLoadMap.expand();
        // UI
        changeMapButton = new UITextButton(gc, Color.WHITE, Color.BLACK, CHANGE_MAP, "Current map :", gc.tileSize, (int)(gc.screenHeight * 0.8), 10, 10);
        UITextButton validateButton = new UITextButton(gc, Color.WHITE, Color.BLACK, VALIDATE, "Load", gc.tileSize, (int)(gc.screenHeight * 0.8) + gc.tileSize, 10, 10);
        // Register load map
        uiFrameLoadMap.addUIObject(changeMapButton, 0, 0);
        uiFrameLoadMap.addUIObject(validateButton, 0, 1);

        // EVENT
        gc.eventUIClick.addListener(this);

        // END
        setShow(false);
    }

    public void reloadAvailableMaps(){
        try {
            foundMapsName = FileUtils.listAllResources("maps").toArray(new String[0]);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void setShow(boolean show) {
        uiFrameLoadMap.setShow(show);

        if (show) {
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

                // Scenery
                gc.sceneryM.changeScenery(SceneryManager.CLEAN_SCENERY);
            }
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

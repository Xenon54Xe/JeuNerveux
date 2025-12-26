package net.rubicon.tile;

import net.rubicon.event.ComponentUIClick;
import net.rubicon.event.IEventComponent;
import net.rubicon.event.IListener;
import net.rubicon.main.GameCanvas;
import net.rubicon.ui.UIObject;
import net.rubicon.ui.UITextButton;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class LoadMapManager implements IListener {

    final GameCanvas gc;

    // PAYLOADS
    public static String CHANGE_MAP = "change-map";
    public static String VALIDATE = "validate-map";
    public static String ACTIVATE_MAP_LOADING = "activate-map-loading";

    // CLASS VARIABLES
    private boolean active;
    private final ArrayList<UIObject> whenActiveObjects = new ArrayList<>();

    private File[] foundMaps;
    private File selectedMap;
    private int index = 0;

    // UI to update
    UITextButton changeMapButton;

    public LoadMapManager(GameCanvas gc){
        this.gc = gc;

        // UI when active
        changeMapButton = new UITextButton(gc, Color.WHITE, Color.BLACK, CHANGE_MAP, "Current map :", gc.tileSize, (int)(gc.screenHeight * 0.8), 10, 10);
        gc.uiM.addUIObject(changeMapButton);
        UITextButton validateButton = new UITextButton(gc, Color.WHITE, Color.BLACK, VALIDATE, "Validate", gc.tileSize, (int)(gc.screenHeight * 0.8) + gc.tileSize, 10, 10);
        gc.uiM.addUIObject(validateButton);
        whenActiveObjects.add(changeMapButton);
        whenActiveObjects.add(validateButton);
        setActive(false);

        // UI
        UITextButton activateLoadMap = new UITextButton(gc, Color.BLACK, Color.WHITE, ACTIVATE_MAP_LOADING, "Activate map loading", gc.tileSize * 10, gc.tileSize * 2, 10, 10);
        gc.uiM.addUIObject(activateLoadMap);

        // EVENT
        gc.eventUIClick.addListener(this);
    }

    public void update(){
        if (active){
            // update change map button
        }
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
            File directory = new File("src/res/maps");
            foundMaps = directory.listFiles();
            assert foundMaps != null: "No map found !!!";
            selectedMap = foundMaps[index];

            changeMapButton.setText("Current map : " + selectedMap.getName());
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

                changeMapButton.setText("Current map : " + selectedMap.getName());
            }
            if (payload.equals(VALIDATE) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {

                // LOAD MAP
                gc.tileM.setMapName(selectedMap.getName());
                gc.tileM.loadMap();
            }
            if (payload.equals(ACTIVATE_MAP_LOADING) && buttonClicked.equals(ComponentUIClick.LEFT_BUTTON)){
                setActive(!active);
            }
        }
    }
}

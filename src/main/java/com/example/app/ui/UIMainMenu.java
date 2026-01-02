package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.SceneryManager;
import com.example.app.event.ComponentUIClick;
import com.example.app.event.IEventComponent;
import com.example.app.event.IListener;
import com.example.app.handler.KeyHandler;
import com.example.app.tile.LoadMapManager;
import com.example.app.tile.MapMakerManager;

import java.awt.*;

public class UIMainMenu implements IListener {

    final GameCanvas gc;

    public final static String START_ADVENTURE = "start-adventure";

    // MAIN MENU
    private final UIFrame mainMenu;

    public UIMainMenu(GameCanvas gc){
        this.gc = gc;


        // UI
        mainMenu = new UIFrame(gc, "Main menu");
        //gc.uiM.addUIObject(mainMenu);
        mainMenu.setShape(1, 4);
        mainMenu.setDrawEvenly();
        mainMenu.expand();
        // TITLE
        UIText title = new UIText(Color.WHITE, "Main Menu",
                gc.screenWidth / 2, gc.tileSize);
        title.setDrawCentered(true);
        // MAP MAKING
        UITextButton activateMapMaking = new UITextButton(gc, Color.BLACK, Color.WHITE,
                MapMakerManager.ACTIVATE_MAPMAKING, "Activate map making",
                gc.screenWidth / 2, gc.tileSize * 2, 10, 10);
        activateMapMaking.setDrawCentered(true);
        // MAP LOADING
        UITextButton activateMapLoading = new UITextButton(gc, Color.BLACK, Color.WHITE,
                LoadMapManager.ACTIVATE_MAP_LOADING, "Activate map loading",
                gc.screenWidth / 2, gc.tileSize * 3, 10, 10);
        activateMapLoading.setDrawCentered(true);
        // START ADVENTURE
        UITextButton startAdventure = new UITextButton(gc, Color.BLACK, Color.WHITE,
                START_ADVENTURE, "Start adventure",
                gc.screenWidth / 2, gc.tileSize * 4, 10, 10);
        startAdventure.setDrawCentered(true);
        // Register
        mainMenu.addUIObject(title, 0, 0);
        mainMenu.addUIObject(activateMapMaking, 0, 1);
        mainMenu.addUIObject(activateMapLoading, 0, 2);
        mainMenu.addUIObject(startAdventure, 0, 3);

        // EVENT
        gc.eventUIClick.addListener(this);
    }

    public void update(){
        if (gc.keyH.getLastKeyCode() == KeyHandler.ESCAPE){
            setShow(!mainMenu.isShow());
        }
    }

    private void hideAll(){
        gc.loadMapM.setActive(false);
        gc.mapMakerM.setActive(false);
    }

    public void setShow(boolean active){
        hideAll();
        mainMenu.setShow(active);
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String mouseButtonClicked)){
            String payload = uiObject.getName();

            if (mouseButtonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {
                if (payload.equals(LoadMapManager.ACTIVATE_MAP_LOADING)
                        || payload.equals(MapMakerManager.ACTIVATE_MAPMAKING)
                        || payload.equals(START_ADVENTURE)){

                    setShow(false);
                    if (payload.equals(LoadMapManager.ACTIVATE_MAP_LOADING)){
                        gc.loadMapM.setActive(true);
                    }
                    else if (payload.equals(MapMakerManager.ACTIVATE_MAPMAKING)){
                        gc.mapMakerM.setActive(true);
                    }
                    else {
                        gc.sceneryM.changeScenery(SceneryManager.MAP1_SCENERY);
                    }
                }
            }
        }
    }
}

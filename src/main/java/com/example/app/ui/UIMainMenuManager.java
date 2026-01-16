package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.SceneryManager;
import com.example.app.event.*;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.handler.KeyHandler;
import com.example.app.tile.LoadMapManager;
import com.example.app.tile.MapDrawerManager;
import com.example.app.tile.MapCreateManager;
import com.example.app.ui.frame.UIFrame;

import java.awt.*;

public class UIMainMenuManager implements IListener {

    final GameCanvas gc;

    public final static String START_ADVENTURE = "start-adventure";
    public final static String TITLE_SCREEN = "title-screen";

    // MAIN MENU
    private final boolean showMapEditors = true;
    private final UIFrame mainMenu;

    public UIMainMenuManager(GameCanvas gc){
        this.gc = gc;

        // UI
        mainMenu = new UIFrame(gc, "Main menu", UIObject.DRAW_CENTER);
        mainMenu.setDrawReference(UIObject.DRAW_CENTER);
        mainMenu.setDrawEvenly();
        mainMenu.expand();
        // TITLE
        UIText title = new UIText(Color.WHITE, "Main Menu",
                gc.screenWidth / 2, gc.tileSize);
        // TITLE SCENERY
        UITextButton titleScreen = new UITextButton(gc, Color.BLACK, Color.WHITE,
                TITLE_SCREEN, "Title screen",
                gc.screenWidth / 2, gc.tileSize * 4, 10, 10);
        // START ADVENTURE
        UITextButton startAdventure = new UITextButton(gc, Color.BLACK, Color.WHITE,
                START_ADVENTURE, "Start adventure",
                gc.screenWidth / 2, gc.tileSize * 4, 10, 10);

        if (showMapEditors) {

            mainMenu.setShape(1, 6);

            // MAP DRAWING
            UITextButton activateMapDrawing = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    MapDrawerManager.ACTIVATE_MAPMAKING, "Draw the map",
                    gc.screenWidth / 2, gc.tileSize * 2, 10, 10);
            // MAP LOADING
            UITextButton activateMapLoading = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    LoadMapManager.ACTIVATE_MAP_LOADING, "Load a map",
                    gc.screenWidth / 2, gc.tileSize * 3, 10, 10);
            // MAP CREATING
            UITextButton activateMapCreating = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    MapCreateManager.ACTIVATE_MAP_CREATING, "Create a map",
                    gc.screenWidth / 2, gc.tileSize * 3, 10, 10);
            // Register
            mainMenu.addUIObject(title, 0, 0);
            mainMenu.addUIObject(startAdventure, 0, 1);
            mainMenu.addUIObject(titleScreen, 0, 2);
            mainMenu.addUIObject(activateMapDrawing, 0, 3);
            mainMenu.addUIObject(activateMapLoading, 0, 4);
            mainMenu.addUIObject(activateMapCreating, 0, 5);
        }
        else {
            mainMenu.setShape(1, 3);

            mainMenu.addUIObject(title, 0, 0);
            mainMenu.addUIObject(startAdventure, 0, 1);
            mainMenu.addUIObject(titleScreen, 0, 2);
        }

        // event
        register(gc.eventUIClick);
    }

    public void update(){
        if (gc.keyH.getLastKeyCode() == KeyHandler.ESCAPE){
            setShow(!mainMenu.isShow());

            if (gc.gameState == GameCanvas.PLAY_STATE){
                gc.gameState = GameCanvas.PAUSE_STATE;
            }else if (gc.gameState == GameCanvas.PAUSE_STATE){
                gc.gameState = GameCanvas.PLAY_STATE;
            }
        }
    }

    private void hideAll(){
        gc.loadMapM.setShow(false);
        gc.mapDrawerM.setShow(false);
        gc.mapCreateM.setShow(false);
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
                        || payload.equals(MapDrawerManager.ACTIVATE_MAPMAKING)
                        || payload.equals(MapCreateManager.ACTIVATE_MAP_CREATING)
                        || payload.equals(START_ADVENTURE)
                        || payload.equals(TITLE_SCREEN)){

                    setShow(false);
                    if (payload.equals(LoadMapManager.ACTIVATE_MAP_LOADING)){
                        gc.loadMapM.setShow(true);
                    }
                    else if (payload.equals(MapDrawerManager.ACTIVATE_MAPMAKING)){
                        gc.mapDrawerM.setShow(true);
                    } else if (payload.equals(MapCreateManager.ACTIVATE_MAP_CREATING)) {
                        gc.mapCreateM.setShow(true);
                    } else if (payload.equals(TITLE_SCREEN)) {
                        gc.sceneryM.changeScenery(SceneryManager.TITLE_SCENERY);
                    } else {
                        gc.sceneryM.changeScenery(SceneryManager.MAP1_SCENERY);
                    }
                    gc.gameState = GameCanvas.PLAY_STATE;
                }
            }
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

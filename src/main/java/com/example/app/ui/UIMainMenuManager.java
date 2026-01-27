// java
package com.example.app.ui;

import com.example.app.GameCanvas;
import com.example.app.Drawable;
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

@Deprecated
public class UIMainMenuManager implements Listener {

    final GameCanvas gc;

    public final static String START_ADVENTURE = "start-adventure";
    public final static String STRENGTH_TEST = "start-strength-test";
    public final static String TITLE_SCREEN = "title-screen";

    // MAIN MENU
    private final UIFrame mainMenu;

    public UIMainMenuManager(GameCanvas gc){
        this.gc = gc;

        // UI
        mainMenu = new UIFrame(gc, "Main menu", Drawable.DRAW_CENTER);
        mainMenu.setDrawRule(UIObject.DRAW_CENTER);
        mainMenu.setDrawEvenly();
        mainMenu.expand();
        // TITLE
        UIText title = new UIText(Color.WHITE, "Main Menu",
                gc.SCREEN_WIDTH / 2, gc.TILE_SIZE);
        // TITLE SCENERY
        UITextButton titleScreen = new UITextButton(gc, Color.BLACK, Color.WHITE,
                TITLE_SCREEN, "Title screen",
                gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 4, 10, 10);
        // START ADVENTURE
        UITextButton startAdventure = new UITextButton(gc, Color.BLACK, Color.WHITE,
                START_ADVENTURE, "Start adventure",
                gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 4, 10, 10);
        UITextButton startStrengthTest = new UITextButton(gc, Color.BLACK, Color.WHITE,
                STRENGTH_TEST, "Start Strength Test",
                gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 4, 10, 10);

        if (gc.editorMode) {

            mainMenu.setShape(1, 7);

            // MAP DRAWING
            UITextButton activateMapDrawing = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    MapDrawerManager.ACTIVATE_MAPMAKING, "Draw the map",
                    gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 2, 10, 10);
            // MAP LOADING
            UITextButton activateMapLoading = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    LoadMapManager.ACTIVATE_MAP_LOADING, "Load a map",
                    gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 3, 10, 10);
            // MAP CREATING
            UITextButton activateMapCreating = new UITextButton(gc, Color.BLACK, Color.WHITE,
                    MapCreateManager.ACTIVATE_MAP_CREATING, "Create a map",
                    gc.SCREEN_WIDTH / 2, gc.TILE_SIZE * 3, 10, 10);
            // Register
            mainMenu.addUIObject(title, 0, 0);
            mainMenu.addUIObject(startAdventure, 0, 1);
            mainMenu.addUIObject(titleScreen, 0, 2);
            mainMenu.addUIObject(activateMapDrawing, 0, 3);
            mainMenu.addUIObject(activateMapLoading, 0, 4);
            mainMenu.addUIObject(activateMapCreating, 0, 5);
            mainMenu.addUIObject(startStrengthTest, 0, 6);
        }
        else {
            mainMenu.setShape(1, 3);

            mainMenu.addUIObject(title, 0, 0);
            mainMenu.addUIObject(startAdventure, 0, 1);
            mainMenu.addUIObject(titleScreen, 0, 2);
        }

        // event
        register();
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
        if (component instanceof ComponentUIClick) {
            ComponentUIClick click = (ComponentUIClick) component;
            // assume ComponentUIClick is a record-like type with accessors uiObject() and mouseButtonClicked()
            UIObject uiObject = click.uiObject();
            String mouseButtonClicked = click.mouseButtonClicked();

            String payload = uiObject.getName();

            if (mouseButtonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {
                if (payload.equals(LoadMapManager.ACTIVATE_MAP_LOADING)
                        || payload.equals(MapDrawerManager.ACTIVATE_MAPMAKING)
                        || payload.equals(MapCreateManager.ACTIVATE_MAP_CREATING)
                        || payload.equals(START_ADVENTURE)
                        || payload.equals(TITLE_SCREEN)
                        || payload.equals(STRENGTH_TEST)){

                    setShow(false);
                    if (payload.equals(LoadMapManager.ACTIVATE_MAP_LOADING)){
                        gc.loadMapM.setShow(true);
                    }
                    else if (payload.equals(MapDrawerManager.ACTIVATE_MAPMAKING)){
                        gc.mapDrawerM.setShow(true);
                    } else if (payload.equals(MapCreateManager.ACTIVATE_MAP_CREATING)) {
                        gc.mapCreateM.setShow(true);
                    } else if (payload.equals(TITLE_SCREEN)) {
                        gc.sceneryM.safeChangeScenery(SceneryManager.TITLE_SCENERY);
                    } else if (payload.equals(START_ADVENTURE)){
                        gc.sceneryM.safeChangeScenery(SceneryManager.MAP1_SCENERY);
                    }else {
                        gc.sceneryM.safeChangeScenery(SceneryManager.STRENGTH_SCENERY);
                    }
                    gc.gameState = GameCanvas.PLAY_STATE;
                }
            }
        }
    }

    // Provide both overloads to match either interface variant.
    public void register(IEvent event) {
        event.addListener(this);
    }

    public void register() {
        // no-op fallback (some Listener variants expect no-arg)
        gc.eventUIClick.addListener(this);
    }
}
package com.example.app;

import com.example.app.entity.Mouse;
import com.example.app.entity.Player;
import com.example.app.event.ComponentUIClick;
import com.example.app.event.IEventComponent;
import com.example.app.event.IListener;
import com.example.app.ui.UIFrame;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;

import java.awt.*;

public class SceneryManager implements IListener {

    final GameCanvas gc;

    private String lastChoosedScenery;

    public static final String TITLE_SCENERY = "TitleScenery";

    public static final String MAP1_SCENERY = "Map1Scenery";
    private final UIFrame uiFrameMap1;

    // UI TO UPDATE
    UIText playerPosition;
    UIText playerXP;

    public SceneryManager(GameCanvas gc){
        this.gc = gc;

        // EVENT
        gc.eventUIClick.addListener(this);

        // UI MENUS
        uiFrameMap1 = new UIFrame(gc, "Scenery Map1");

        // UI
        makeUI();
    }

    private void makeUI(){
        // MAP1
        uiFrameMap1.setShape(1, 2);
        uiFrameMap1.setDrawEvenly();
        playerPosition = new UIText(Color.WHITE, "Position : ", gc.tileSize, gc.tileSize);
        playerXP = new UIText(Color.WHITE, "XP : ", gc.tileSize, gc.tileSize * 2);
        uiFrameMap1.addUIObject(playerPosition, 0, 0);
        uiFrameMap1.addUIObject(playerXP, 0, 1);
    }

    public void changeScenery(String scenery){

        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY)) {

            lastChoosedScenery = scenery;

            hideAllUI();
            gc.entityM.safeRemoveAllEntities();

            if (scenery.equals(TITLE_SCENERY)) {
                titleScenery();
            } else {
                map1Scenery();
            }
        }
    }

    private void hideAllUI(){
        uiFrameMap1.setShow(false);
    }

    private void titleScenery(){
        // MAP
        gc.tileM.setMapName(TITLE_SCENERY);
        gc.tileM.loadMap();

        // ENTITY MANAGER
        // MICE
        Rectangle mouseSolidArea = new Rectangle(16, 32, 16, 16);
        for (int i = 0; i < 5; i++) {
            Mouse mouse = new Mouse(gc, mouseSolidArea, "mouse" + i,50, gc.tileSize, gc.tileSize, 25, 180, 1, gc.tileSize, 2);
            mouse.setRandomPosition(gc.tileM.spawnableTiles);
            gc.entityM.addEntity(mouse);
        }

        // TRACKED
        gc.entityM.randomChangeTracked();
    }

    private void map1Scenery(){
        // MAP
        gc.tileM.setMapName(MAP1_SCENERY);
        gc.tileM.loadMap();

        // ENTITY MANAGER
        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        Player player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 10000, 6, 0, 2 * gc.tileSize, 20);
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);
        // MICE
        Rectangle mouseSolidArea = new Rectangle(16, 32, 16, 16);
        for (int i = 0; i < 100; i++) {
            Mouse mouse = new Mouse(gc, mouseSolidArea, "mouse" + i,50, gc.tileSize, gc.tileSize, 25, 180, 1, gc.tileSize, 2);
            //mouse.setTilePosition(5, 3);
            mouse.setRandomPosition(gc.tileM.spawnableTiles);
            gc.entityM.addEntity(mouse);
        }

        // TRACKED
        gc.setTracked(player);

        // UI
        uiFrameMap1.setShow(true);
    }

    public void update(){

        if (lastChoosedScenery.equals(MAP1_SCENERY)) {
            playerPosition.setText("Position : " + gc.entityM.player.getTileX() + ", " + gc.entityM.player.getTileY());
            playerXP.setText("XP : " + gc.entityM.player.getXp());
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String mouseButtonClicked)){
            String payload = uiObject.getName();

            if (mouseButtonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {
                changeScenery(payload);
            }
        }
    }
}

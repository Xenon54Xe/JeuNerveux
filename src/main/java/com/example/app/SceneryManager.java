package com.example.app;

import com.example.app.entity.*;
import com.example.app.event.*;
import com.example.app.event.Event;
import com.example.app.ui.UIFrame;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;
import com.example.app.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class SceneryManager implements IListener {

    final GameCanvas gc;

    public static final String TITLE_SCENERY = "TitleScenery";
    public static final String MAP1_SCENERY = "Map1Scenery";
    public static final String CLEAN_SCENERY = "clean-scenery";

    // CLASS VARIABLES
    private String lastChoosedScenery;
    private final UIFrame uiFrameMap1;

    private final ArrayList<IEntityGroup> groups = new ArrayList<>();

    // UI TO UPDATE
    UIText playerPosition;
    UIText playerXP;

    public SceneryManager(GameCanvas gc){
        this.gc = gc;

        // UI MENUS
        uiFrameMap1 = new UIFrame(gc, "Scenery Map1", UIObject.DRAW_TOP_LEFT_CORNER,
                0, 0, gc.tileSize, gc.tileSize * 2, 1, 2);
        uiFrameMap1.setDrawEvenly();

        // UI
        makeUI();

        // EVENT
        register(gc.eventUIClick);
    }

    private void makeUI(){
        // MAP1
        playerPosition = new UIText(Color.WHITE, "Position : ", gc.tileSize, gc.tileSize);
        playerXP = new UIText(Color.WHITE, "XP : ", gc.tileSize, gc.tileSize * 2);
        uiFrameMap1.addUIObject(playerPosition, 0, 0);
        uiFrameMap1.addUIObject(playerXP, 0, 1);
    }

    public void changeScenery(String scenery){

        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY) || scenery.equals(CLEAN_SCENERY)) {

            lastChoosedScenery = scenery;

            hideAllUI();
            gc.entityM.safeRemoveAllEntities();

            if (scenery.equals(TITLE_SCENERY)) {
                titleScenery();
            } else if (scenery.equals(MAP1_SCENERY)){
                map1Scenery();
            }
            else {
                cleanScenery();
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
        Player player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 100, 6, 0, 2 * gc.tileSize, 20);
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);


        // MICE
        Rectangle mouseSolidArea = new Rectangle(16, 32, 16, 16);
        for (int i = 0; i < 20; i++) {

            /*
            /!\ les groups ne sont jamais détruits !
             */
            IEntityGroup group = new EnemyEntityGroup(gc);
            groups.add(group);
            Vector2D targetPosition = Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles);
            for (int j = 0; j < 5; j++) {

                Mouse mouse = new Mouse(gc, mouseSolidArea, "mouse-" + (5 * i + j),50, gc.tileSize, gc.tileSize, 25, 180, 1, gc.tileSize, 5);
                mouse.setWorldPosition(targetPosition.add(Vector2D.getRandomVectorNormalized().mul(gc.tileSize)));
                group.addEntity(mouse);
            }
        }

        // TRACKED
        gc.setTracked(player);

        // UI
        uiFrameMap1.setShow(true);
    }

    private void cleanScenery(){
        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        Player player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 10000, 6, 0, 2 * gc.tileSize, 20);
        player.setRandomPosition(gc.tileM.spawnableTiles);
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);

        // TRACKED
        gc.setTracked(player);
    }

    public void update(){

        if (lastChoosedScenery.equals(MAP1_SCENERY)) {
            playerPosition.setText("Position : " + gc.entityM.player.getTileX() + ", " + gc.entityM.player.getTileY());
            playerXP.setText("XP : " + gc.entityM.player.getXp());

            // ENTITY GROUP
            for (IEntityGroup group : groups){
                group.update();
            }
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String mouseButtonClicked)){
            String payload = uiObject.getName();

            if (mouseButtonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {
                changeScenery(payload);
            }
        } else if (component instanceof ComponentGroupDead(EntityGroup group)) {
            if (! groups.remove(group)){
                assert false;
            }
        }
    }

    @Override
    public void register(Event event) {
        event.addListener(this);
    }
}

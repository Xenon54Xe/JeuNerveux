package com.example.app;

import com.example.app.entity.*;
import com.example.app.entity.animals.*;
import com.example.app.entity.group.AnimalEntityGroup;
import com.example.app.entity.group.EntityGroup;
import com.example.app.entity.group.PlayerEntityGroup;
import com.example.app.event.*;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.frame.UIFrame;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;
import com.example.app.utils.Vector2D;

import java.awt.*;

public class SceneryManager implements Manager, Listener {

    final GameCanvas gc;

    // STATIC
    public static final String TITLE_SCENERY = "TitleScenery";
    public static final String MAP1_SCENERY = "map2-sav1";
    public static final String STRENGTH_SCENERY = "Map1Scenery";
    public static final String CLEAN_SCENERY = "clean-scenery";

    // CLASS VARIABLES
    private int changeSceneryCounter = 1;
    private String lastChosenScenery;

    // LOADING
    private UIFrame loadingFrame;
    // Win frame, last for 5 seconds
    private UIFrame winFrame;
    private final int winDelay = 300;
    private int winCounter = 0;
    // MAP1
    private UIFrame uiFrameMap1;

    // UI TO UPDATE
    UIText playerPosition;
    UIText playerMobs;

    public SceneryManager(GameCanvas gc){
        this.gc = gc;

        // EVENT
        register();
    }

    @Override
    public void init() {
        // Loading FRAME
        loadingFrame = new UIFrame(gc, "Default Frame", UIObject.DRAW_CENTER);
        loadingFrame.setDrawEvenly();
        loadingFrame.setShape(1, 1);
        // Loading
        UIText loadingText = new UIText(Color.WHITE, "LOADING...", gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2);
        loadingFrame.addUIObject(loadingText, 0, 0);

        // win frame
        winFrame = new UIFrame(gc, "Win Frame", UIObject.DRAW_CENTER);
        winFrame.setDrawEvenly();
        winFrame.setShape(1, 1);
        UIText winText = new UIText(Color.YELLOW, "YOU WIN!", gc.SCREEN_WIDTH / 2, gc.SCREEN_HEIGHT / 2);
        winFrame.addUIObject(winText, 0, 0);

        // map1 frame
        uiFrameMap1 = new UIFrame(gc, "Scenery Map1", UIObject.DRAW_TOP_LEFT,
                0, 0, gc.TILE_SIZE, gc.TILE_SIZE * 2, 1, 2);
        uiFrameMap1.setDrawEvenly();
        // MAP1
        playerPosition = new UIText(Color.WHITE, "Position : ", gc.TILE_SIZE, gc.TILE_SIZE);
        playerMobs = new UIText(Color.WHITE, "0/10", gc.TILE_SIZE, gc.TILE_SIZE * 2);
        uiFrameMap1.addUIObject(playerPosition, 0, 0);
        uiFrameMap1.addUIObject(playerMobs, 0, 1);
    }

    public void safeChangeScenery(String scenery){

        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY) || scenery.equals(CLEAN_SCENERY) || scenery.equals(STRENGTH_SCENERY)) {

            // SHOW chargement
            loadingFrame.setShow(true);
            lastChosenScenery = scenery;
            changeSceneryCounter = 2;
        }
    }

    public void changeScenery(String scenery){
        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY) || scenery.equals(CLEAN_SCENERY) || scenery.equals(STRENGTH_SCENERY)) {
            lastChosenScenery = scenery;

            hideAllUI();
            gc.entityM.safeRemoveAllEntities();

            switch (scenery) {
                case TITLE_SCENERY -> titleScenery();
                case MAP1_SCENERY -> map1Scenery();
                case CLEAN_SCENERY -> cleanScenery();
                default -> strengthScenery();
            }

            loadingFrame.setShow(false);
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
        for (int i = 0; i < 5; i++) {
            Mouse mouse = new Mouse(gc, "mouse" + i);
            mouse.setRandomTilePosition(gc.tileM.spawnableTiles);
            gc.entityM.addEntity(mouse);
        }

        // TRACKED
        gc.entityM.trackFirstFound();
    }

    private void map1Scenery(){
        // MAP
        gc.tileM.setMapName(MAP1_SCENERY);
        gc.tileM.loadMap();

        // ENTITY MANAGER
        // PLAYER
        Player player = new Player(gc, "Player");
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);

        // TRACKED
        gc.setTracked(player);

        // UI
        uiFrameMap1.setShow(true);
    }

    private void cleanScenery(){
        // PLAYER
        Player player = new Player(gc, "Player", 200, 10000, 6, 0, 2 * gc.TILE_SIZE, 20);
        player.setRandomTilePosition(gc.tileM.spawnableTiles);
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);

        // TRACKED
        gc.setTracked(player);
    }

    public void strengthScenery(){
        // PLAYER
        Player player = new Player(gc, "Player");
        player.setRandomTilePosition(gc.tileM.spawnableTiles);
        player.setDamage(100000);
        player.setAttackDelay(1);
        player.setMaxHealth(1000000000);
        gc.entityM.addEntity(player);
        gc.entityM.setPlayer(player);

        // TRACKED
        gc.setTracked(player);

        gc.tileM.loadMap("EmptyScenery");
    }

    private void makeEntityGroup(LivingEntity template, Vector2D position, int count){
        EntityGroup group = new AnimalEntityGroup(gc);

        for (int i = 0; i < count; i++) {

            Entity entity = template.makeClone();
            entity.setWorldPosition(position.getX(), position.getY());
            group.safeAddEntity(entity);
        }
    }

    public LivingEntity intToEntity(int i){
        return switch (i){
            case 0 -> new Mouse(gc, "mouse");
            case 1 -> new Rat(gc, "rat");
            case 2 -> new Rabbit(gc, "rabbi");
            case 3 -> new Cat(gc, "cat");
            case 4 -> new Dog(gc, "dog");
            case 5 -> new Fox(gc, "fox");
            case 6 -> new Wolf(gc, "wolf");
            case 7 -> new Bear(gc, "bear");
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };
    }

    public int entityToInt(LivingEntity entity){
        return switch (entity) {
            case Mouse mouse -> 0;
            case Rat rat -> 1;
            case Rabbit rabbit -> 2;
            case Cat cat -> 3;
            case Dog dog -> 4;
            case Fox fox -> 5;
            case Wolf wolf -> 6;
            case Bear bear -> 7;
            case null, default -> -1;
        };
    }

    public void win(){
        winFrame.setShow(true);
        winCounter = winDelay;
    }

    public void update(){

        if (lastChosenScenery.equals(TITLE_SCENERY)){
            if (gc.gameState == GameCanvas.PAUSE_STATE){
                gc.gameState = GameCanvas.PLAY_STATE;
            }
        }

        if (lastChosenScenery.equals(MAP1_SCENERY)) {

            if (gc.entityM.player != null) {
                playerPosition.setText("Position : " + gc.entityM.player.getTileX() + ", " + gc.entityM.player.getTileY());
                playerMobs.setText(gc.entityM.player.playerEntityGroup.size() + "/10");
            }
        }

        if (lastChosenScenery.equals(STRENGTH_SCENERY)) {

            if (gc.entityM.player != null) {
                playerPosition.setText("Position : " + gc.entityM.player.getTileX() + ", " + gc.entityM.player.getTileY());
                playerMobs.setText("XP : " + gc.entityM.player.getXp());
            }
        }

        if (changeSceneryCounter > 0){
            changeSceneryCounter--;
            if (changeSceneryCounter == 0){
                changeScenery(lastChosenScenery);
            }
        }

        // Update temporary win frame
        if (winCounter > 0){
            winCounter--;
            if (winCounter == 0){
                winFrame.setShow(false);
            }
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentUIClick(UIObject uiObject, String mouseButtonClicked)){
            String payload = uiObject.getName();

            if (mouseButtonClicked.equals(ComponentUIClick.LEFT_BUTTON)) {
                safeChangeScenery(payload);
            }
        }
    }

    @Override
    public void register() {
        gc.eventUIClick.addListener(this);
    }
}

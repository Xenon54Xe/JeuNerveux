package com.example.app;

import com.example.app.entity.*;
import com.example.app.entity.animals.*;
import com.example.app.entity.group.AnimalEntityGroup;
import com.example.app.entity.group.EntityGroup;
import com.example.app.entity.group.IEntityGroup;
import com.example.app.entity.group.PlayerEntityGroup;
import com.example.app.event.*;
import com.example.app.event.component.ComponentGroupDead;
import com.example.app.event.component.ComponentUIClick;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.frame.UIFrame;
import com.example.app.ui.UIObject;
import com.example.app.ui.UIText;
import com.example.app.utils.ILinkedList;
import com.example.app.utils.LinkedList;
import com.example.app.utils.Vector2D;

import java.awt.*;

public class SceneryManager implements IListener {

    final GameCanvas gc;

    public static final String TITLE_SCENERY = "TitleScenery";
    public static final String MAP1_SCENERY = "map2-sav1";
    public static final String STRENGTH_SCENERY = "Map1Scenery";
    public static final String CLEAN_SCENERY = "clean-scenery";

    // CLASS VARIABLES
    private int changeSceneryCount = 1;
    private String lastChosenScenery;
    private final UIFrame uiFrameMap1;
    private final UIText loadingText;

    public final ILinkedList<IEntityGroup> groups = new LinkedList<>();
    private int[] animalGroupCount = new int[8];

    // UI TO UPDATE
    UIText playerPosition;
    UIText playerMobs;

    public SceneryManager(GameCanvas gc){
        this.gc = gc;

        // UI MENUS
        uiFrameMap1 = new UIFrame(gc, "Scenery Map1", UIObject.DRAW_TOP_LEFT_CORNER,
                0, 0, gc.tileSize, gc.tileSize * 2, 1, 2);
        uiFrameMap1.setDrawEvenly();

        // UI
        makeUI();
        // Chargement
        loadingText = new UIText(Color.WHITE, "LOADING...", gc.screenWidth / 2, gc.screenHeight / 2);
        loadingText.setDrawReference(UIObject.DRAW_CENTER);
        gc.uiM.addUIObject(loadingText);

        // EVENT
        register(gc.eventUIClick);
        register(gc.eventGroupDead);
    }

    private void makeUI(){
        // MAP1
        playerPosition = new UIText(Color.WHITE, "Position : ", gc.tileSize, gc.tileSize);
        playerMobs = new UIText(Color.WHITE, "0/10", gc.tileSize, gc.tileSize * 2);
        uiFrameMap1.addUIObject(playerPosition, 0, 0);
        uiFrameMap1.addUIObject(playerMobs, 0, 1);
    }

    public void safeChangeScenery(String scenery){

        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY) || scenery.equals(CLEAN_SCENERY) || scenery.equals(STRENGTH_SCENERY)) {

            // SHOW chargement
            loadingText.setShow(true);
            lastChosenScenery = scenery;
            changeSceneryCount = 2;
        }
    }

    public void changeScenery(String scenery){
        if (scenery.equals(TITLE_SCENERY) || scenery.equals(MAP1_SCENERY) || scenery.equals(CLEAN_SCENERY) || scenery.equals(STRENGTH_SCENERY)) {
            lastChosenScenery = scenery;

            hideAllUI();
            gc.entityM.safeRemoveAllEntities();

            if (scenery.equals(TITLE_SCENERY)) {
                titleScenery();
            } else if (scenery.equals(MAP1_SCENERY)) {
                map1Scenery();
            } else if (scenery.equals(CLEAN_SCENERY)){
                cleanScenery();
            }else {
                strengthScenery();
            }

            loadingText.setShow(false);
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
        gc.entityM.trackRandom();
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

        groups.clear();
        animalGroupCount = new int[8];
    }

    private void cleanScenery(){
        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        Player player = new Player(gc, "Player", 200, 10000, 6, 0, 2 * gc.tileSize, 20);
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

        groups.clear();
        animalGroupCount = new int[8];
    }

    private void makeEntityGroup(LivingEntity template, Vector2D position, int count){
        IEntityGroup group = new AnimalEntityGroup(gc);
        groups.add(group);

        for (int i = 0; i < count; i++) {

            Entity entity = template.makeClone();
            entity.setWorldPosition(position);
            group.addEntity((LivingEntity) entity);
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

            if (gc.gameState == GameCanvas.PLAY_STATE) {
                // ENTITY GROUP
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).update();
                }

                if (gc.entityM.player != null) {
                    // MAKE GROUPS SPAWN AROUND PLAYER
                    // Mouse to Rabbit
                    for (int i = 0; i <= 2; i++) {
                        int groupCount = animalGroupCount[i];
                        if (groupCount < 10) {
                            animalGroupCount[i]++;
                            LivingEntity template = intToEntity(i);

                            int count = (int) (Math.random() * 3) + 3;
                            makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                        }
                    }
                    // Cat to dog
                    for (int i = 3; i <= 4; i++) {
                        int groupCount = animalGroupCount[i];
                        if (groupCount < 5) {
                            animalGroupCount[i]++;
                            LivingEntity template = intToEntity(i);

                            int count = (int) (Math.random() * 3) + 3;
                            makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                        }
                    }
                    // Fox to wolf
                    for (int i = 5; i <= 6; i++) {
                        int groupCount = animalGroupCount[i];
                        if (groupCount < 2) {
                            animalGroupCount[i]++;
                            LivingEntity template = intToEntity(i);

                            int count = (int) (Math.random() * 3) + 3;
                            makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                        }
                    }
                    int groupCount = animalGroupCount[7];
                    if (groupCount < 1) {
                        animalGroupCount[7]++;
                        LivingEntity template = intToEntity(7);

                        int count = 1;
                        makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                    }
                }
            }
        }

        if (lastChosenScenery.equals(STRENGTH_SCENERY)) {

            if (gc.entityM.player != null) {
                playerPosition.setText("Position : " + gc.entityM.player.getTileX() + ", " + gc.entityM.player.getTileY());
                playerMobs.setText("XP : " + gc.entityM.player.getXp());
            }

            if (gc.gameState == GameCanvas.PLAY_STATE) {
                // ENTITY GROUP
                for (int i = 0; i < groups.size(); i++) {
                    groups.get(i).update();
                }

                if (gc.entityM.player != null) {
                    // MAKE GROUPS SPAWN AROUND PLAYER
                    // Wolf
                    int nbA = 6;
                    int countA = 10;
                    int groupCount = animalGroupCount[nbA];
                    if (groupCount < 1) {
                        animalGroupCount[nbA]++;
                        LivingEntity template = intToEntity(nbA);

                        int count = countA;
                        makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                    }
                    // Wolf
                    int nbB = nbA + 1;
                    int countB = 1;
                    groupCount = animalGroupCount[nbB];
                    if (groupCount < 1) {
                        animalGroupCount[nbB]++;
                        LivingEntity template = intToEntity(nbB);

                        int count = countB;
                        makeEntityGroup(template, Vector2D.chooseRandomWorldPosition(gc, gc.tileM.spawnableTiles), count);
                    }
                }
            }
        }

        if (changeSceneryCount > 0){
            changeSceneryCount --;
            if (changeSceneryCount == 0){
                changeScenery(lastChosenScenery);
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
        } else if (component instanceof ComponentGroupDead(EntityGroup group)) {
            if (group instanceof PlayerEntityGroup){
                return;
            }

            assert groups.remove(group);
            animalGroupCount[group.animalType]--;
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

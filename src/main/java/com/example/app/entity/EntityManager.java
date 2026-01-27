package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.entity.group.EntityGroup;
import com.example.app.event.*;
import com.example.app.event.component.ComponentChangeMap;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.ComponentGroupDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.ILoopList;
import com.example.app.utils.LoopList;

import java.awt.*;

public class EntityManager implements Listener {

    final GameCanvas gc;

    // CLASS VARIABLES
    public final ILoopList<LivingEntity> entitiesToAdd = new LoopList<>();
    public final ILoopList<LivingEntity> livingEntities = new LoopList<>();
    private final ILoopList<LivingEntity> entitiesToRemove = new LoopList<>();

    public Player player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // EVENT
        register();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void addEntity(LivingEntity entity){
        livingEntities.add(entity);
    }

    public void safeAddEntity(LivingEntity entity){
        entitiesToAdd.add(entity);
    }

    private void removeEntity(LivingEntity entity){
        livingEntities.remove(entity);
    }

    public void safeRemoveEntity(LivingEntity entity){
        // Add the com.example.app.entity to the list of com.example.app.entity to remove
        if (!entitiesToRemove.contains(entity)) {
            entitiesToRemove.add(entity);
        }
    }

    public void safeRemoveAllEntities(){
        for (int i = 0; i < livingEntities.size(); i++){
            livingEntities.get(true).softKill(null);
        }
    }

    public void trackPlayer(){
        if (player != null) {
            gc.tracked = player;
        }
    }

    public void trackRandom() {
        for (int i = 0; i < livingEntities.size(); i++){
            LivingEntity entity = livingEntities.get(true);
            if (entity != gc.tracked && !entitiesToRemove.contains(entity)){
                gc.tracked = entity;
                return;
            }
        }
    }

    public void regenAllEntities(){
        for (int i = 0; i < livingEntities.size(); i++) {
            LivingEntity entity = livingEntities.get(true);
            entity.setHealth(entity.getMaxHealth());
        }
    }

    public void update(){
        // Remove entities
        if (!entitiesToRemove.isEmpty()) {
            for (int i = 0; i < entitiesToRemove.size(); i++) {
                removeEntity(entitiesToRemove.get(true));
            }
            entitiesToRemove.clear();
        }

        // Add entities
        if (!entitiesToAdd.isEmpty()) {
            livingEntities.addAll(entitiesToAdd.toArray());
            entitiesToAdd.clear();
        }

        // Update entities
        for (int i = 0; i < livingEntities.size(); i++){
            livingEntities.get(true).update();
        }

        if (gc.keyH.fClicked){
            trackRandom();
        }
        if (gc.keyH.gClicked){
            trackPlayer();
        }
        if(gc.keyH.rClicked && gc.editorMode){
            regenAllEntities();
        }

        // Update ui map
        //gc.uiM.uiMap.setEntitiesPositions(livingEntities);
    }

    public void draw(Graphics2D g2){
        if (player != null) {
            livingEntities.setRoot(player);
            livingEntities.shift();
        }

        for (int i = 0; i < livingEntities.size(); i++){
            livingEntities.get(true).draw(g2);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead edComponent) {
            LivingEntity deadEntity = edComponent.deadEntity();

            safeRemoveEntity(deadEntity);

            if (deadEntity.equals(gc.tracked)) {
                trackRandom();
            }

            if (player != null && deadEntity.equals(player)){
                player = null;
            }
        }
        if (component instanceof ComponentChangeMap cmComponent){
            for (int i = 0; i < livingEntities.size(); i++){
                livingEntities.get(true).setRandomTilePosition(cmComponent.spawnableTiles());
            }
        }
        if (component instanceof ComponentGroupDead(EntityGroup group)){
            for (LivingEntity entity : group.entities){
                removeEntity(entity);
            }
        }
    }

    @Override
    public void register() {
        gc.eventChangeMap.addListener(this);
        gc.eventEntityDead.addListener(this);
    }
}

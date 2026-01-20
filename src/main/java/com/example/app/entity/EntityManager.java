package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.entity.group.EntityGroup;
import com.example.app.event.*;
import com.example.app.event.component.ComponentChangeMap;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.ComponentGroupDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.ILinkedList;
import com.example.app.utils.LinkedList;

import java.awt.*;

public class EntityManager implements IListener {

    final GameCanvas gc;

    // CLASS VARIABLES
    public final ILinkedList<LivingEntity> entitiesToAdd = new LinkedList<>();
    public final ILinkedList<LivingEntity> livingEntities = new LinkedList<>();
    private final ILinkedList<LivingEntity> entitiesToRemove = new LinkedList<>();

    public Player player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // EVENT
        register(gc.eventChangeMap);
        register(gc.eventEntityDead);
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
            livingEntities.getFirstValueNShift().softKill(null);
        }
    }

    public void trackPlayer(){
        if (player != null) {
            gc.tracked = player;
        }
    }

    public void trackRandom() {
        for (int i = 0; i < livingEntities.size(); i++){
            LivingEntity entity = livingEntities.getFirstValueNShift();
            if (entity != gc.tracked && !entitiesToRemove.contains(entity)){
                gc.tracked = entity;
                return;
            }
        }
    }

    public void regenAllEntities(){
        for (int i = 0; i < livingEntities.size(); i++) {
            LivingEntity entity = livingEntities.getFirstValueNShift();
            entity.setHealth(entity.getMaxHealth());
        }
    }

    public void update(){
        // Remove entities
        if (!entitiesToRemove.isEmpty()) {
            for (int i = 0; i < entitiesToRemove.size(); i++) {
                removeEntity(entitiesToRemove.getFirstValueNShift());
            }
            entitiesToRemove.clear();
        }

        // Add entities
        if (!entitiesToAdd.isEmpty()) {
            livingEntities.addAll(entitiesToAdd);
            entitiesToAdd.clear();
        }

        // Update entities
        for (int i = 0; i < livingEntities.size(); i++){
            livingEntities.getFirstValueNShift().update();
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
            livingEntities.getFirstValueNShift().draw(g2);
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
                livingEntities.getFirstValueNShift().setRandomTilePosition(cmComponent.spawnableTiles());
            }
        }
        if (component instanceof ComponentGroupDead(EntityGroup group)){
            for (LivingEntity entity : group.entities){
                removeEntity(entity);
            }
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

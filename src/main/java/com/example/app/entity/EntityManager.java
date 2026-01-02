package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.event.ComponentChangeMap;
import com.example.app.event.ComponentEntityDead;
import com.example.app.event.IEventComponent;
import com.example.app.event.IListener;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager implements IListener {

    final GameCanvas gc;

    // CLASS VARIABLES
    public final ArrayList<LivingEntity> livingEntities = new ArrayList<>();
    private final ArrayList<LivingEntity> entitiesToRemove = new ArrayList<>();

    public LivingEntity player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // EVENT
        gc.eventEntityDead.addListener(this);
        gc.eventChangeMap.addListener(this);
    }

    public void setPlayer(LivingEntity player) {
        this.player = player;
    }

    public void addEntity(LivingEntity entity){
        livingEntities.add(entity);
    }

    private void removeEntity(LivingEntity entity){
        livingEntities.remove(entity);
    }

    public void safeRemoveEntity(LivingEntity entity){
        // Add the com.example.app.entity to the list of com.example.app.entity to remove
        entitiesToRemove.add(entity);
    }

    public void safeRemoveAllEntities(){
        for (LivingEntity entity : livingEntities){
            if (!entitiesToRemove.contains(entity)){
                entitiesToRemove.add(entity);
            }
        }
    }

    public void randomChangeTracked() {
        for (LivingEntity entity : livingEntities){
            if (entity != gc.tracked && !entitiesToRemove.contains(entity)){
                gc.tracked = entity;
                return;
            }
        }
    }

    public void update(){
        // Remove entities
        for (LivingEntity entity : entitiesToRemove){
            removeEntity(entity);
        }
        entitiesToRemove.clear();

        // Update entities
        for (LivingEntity entity : livingEntities){
            entity.update();
        }

        if (gc.keyH.fClicked){
            randomChangeTracked();
        }
    }

    public void draw(Graphics2D g2){
        for (LivingEntity entity : livingEntities){
            entity.draw(g2);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead edComponent) {
            LivingEntity deadEntity = edComponent.deadEntity();

            safeRemoveEntity(deadEntity);

            if (deadEntity.equals(gc.tracked)) {
                randomChangeTracked();
            }
        }
        if (component instanceof ComponentChangeMap cmComponent){
            for (Entity entity : livingEntities){
                entity.setRandomPosition(cmComponent.spawnableTiles());
            }
        }
    }
}

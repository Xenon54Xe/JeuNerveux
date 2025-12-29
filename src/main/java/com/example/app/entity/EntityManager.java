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

        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 10000, 0, 2 * gc.tileSize, 20);
        addEntity(player);

        Rectangle mouseSolidArea = new Rectangle(16, 32, 16, 16);
        // MICE
        for (int i = 0; i < 0; i++) {
            Mouse mouse = new Mouse(gc, mouseSolidArea, "mouse" + i,50, gc.tileSize, gc.tileSize, 25, 1, gc.tileSize, 2);
            //mouse.setTilePosition(5, 3);
            mouse.setRandomPosition(gc.tileM.spawnableTiles);
            addEntity(mouse);
        }

        // TRACKED
        gc.setTracked(player);

        // EVENT
        gc.eventEntityDead.addListener(this);
        gc.eventChangeMap.addListener(this);
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

    public void update(double dt){
        // Remove entities
        for (LivingEntity entity : entitiesToRemove){
            removeEntity(entity);
        }
        entitiesToRemove.clear();

        // Update entities
        for (LivingEntity entity : livingEntities){
            entity.update(dt);
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

    private void randomChangeTracked() {
        for (LivingEntity entity : livingEntities){
            if (entity != gc.tracked && !entitiesToRemove.contains(entity)){
                gc.tracked = entity;
                return;
            }
        }
    }
}

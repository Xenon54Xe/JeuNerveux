package net.rubicon.entity;

import net.rubicon.event.ECEntityDead;
import net.rubicon.event.IListener;
import net.rubicon.main.GameCanvas;
import net.rubicon.utils.Vector2D;

import java.awt.*;
import java.util.ArrayList;

public class EntityManager implements IListener<ECEntityDead> {

    final GameCanvas gc;

    // CLASS VARIABLES
    public ArrayList<LivingEntity> livingEntities = new ArrayList<>();
    private ArrayList<LivingEntity> entitiesToRemove = new ArrayList<>();

    public LivingEntity player;

    public EntityManager(GameCanvas gc){
        this.gc = gc;

        // PLAYER
        Rectangle playerSolidArea = new Rectangle(8, 16, 32, 32);
        player = new Player(gc, playerSolidArea, "Player", 200, gc.tileSize, gc.tileSize, 100, 0, 2 * gc.tileSize, 20);
        addEntity(player);

        Rectangle mouseSolidArea = new Rectangle(16, 32, 16, 16);
        // MICE
        for (int i = 0; i < 1; i++) {
            Mouse mouse = new Mouse(gc, mouseSolidArea, "mouse" + i, 50, gc.tileSize, gc.tileSize, 25, 1, gc.tileSize, 2);
            mouse.setWorldPosition(new Vector2D(gc.tileSize * 11, gc.tileSize * 12));
            //mouse.setRandomPosition();
            addEntity(mouse);
        }

        // TRACKED
        gc.setTracked(player);

        // EVENT
        gc.eventEntityDead.addListener(this);
    }

    public void addEntity(LivingEntity entity){
        livingEntities.add(entity);
    }

    public void removeEntity(LivingEntity entity){
        // Add the entity to the list of entity to remove
        entitiesToRemove.add(entity);
    }

    public void update(double dt){
        // Remove entities
        for (LivingEntity entity : entitiesToRemove){
            livingEntities.remove(entity);
        }
        entitiesToRemove.clear();

        // Update entities
        for (LivingEntity entity : livingEntities){
            entity.update(dt);
        }
    }

    public void draw(Graphics2D g2){
        for (LivingEntity entity : livingEntities){
            entity.draw(g2);
        }
    }

    @Override
    public void onTrigger(ECEntityDead component) {
        LivingEntity deadEntity = component.deadEntity();
        removeEntity(deadEntity);

        if (deadEntity.equals(gc.tracked)){
            for (LivingEntity entity : livingEntities){
                if (!entitiesToRemove.contains(entity)){
                    gc.tracked = entity;
                    return;
                }
            }
        }
    }
}

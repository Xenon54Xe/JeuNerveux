package com.example.app.entity;

import com.example.app.GameCanvas;
import com.example.app.event.*;
import com.example.app.ui.IUpdatable;
import com.example.app.utils.Vector2D;

import java.util.ArrayList;

public abstract class EntityGroup implements IListener, IEntityGroup {

    final GameCanvas gc;

    // STATIC
    private static int HIGHEST_ID = -1;

    // CLASS VARIABLES
    public final int ID;

    private final ArrayList<LivingEntity> entities = new ArrayList<>();
    private int entityCount = 0;

    public EntityGroup(GameCanvas gc){
        ID = HIGHEST_ID + 1;
        HIGHEST_ID = ID;

        this.gc = gc;

        register(gc.eventEntityDead);
    }

    @Override
    public void addEntity(LivingEntity entity){
        entity.setOwnBehavior(false);
        entity.setGroupID(ID);

        gc.entityM.safeAddEntity(entity);

        entities.add(entity);
        entityCount++;
    }

    @Override
    public boolean removeEntity(LivingEntity entity){
        if (entities.remove(entity)){
            entityCount--;

            if(entityCount == 0) {
                killGroup();
            }
            return true;
        }
        return false;
    }

    public void killAllEntities(){
        for (LivingEntity entity : entities){
            entity.softKill();
        }
        entities.clear();
    }

    public void killGroup(){
        killAllEntities();
        gc.eventGroupDead.trigger(new ComponentGroupDead(this));
    }

    public boolean contains(LivingEntity entity){
        return entities.contains(entity);
    }

    private void makeEntitiesMove(GameCanvas gc, Vector2D targetPosition, ArrayList<LivingEntity> entities){
        for (LivingEntity entity : entities){
            Vector2D position = entity.getWorldPosition();
            Vector2D targetDirection = targetPosition.sub(position).getNormalized();
            entity.setMoveDirectionVector(targetDirection);
            entity.move(gc.dt);
        }
    }

    public void makeEntitiesMove(GameCanvas gc, Vector2D targetPosition) {
        makeEntitiesMove(gc, targetPosition, entities);
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity deadEntity, LivingEntity killer)){
            removeEntity(deadEntity);
        }
    }

    @Override
    public void register(Event event) {
        event.addListener(this);
    }
}

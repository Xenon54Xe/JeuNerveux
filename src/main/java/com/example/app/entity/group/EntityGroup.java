package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.Entity;
import com.example.app.entity.LivingEntity;
import com.example.app.event.*;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.ComponentGroupDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.utils.Vector2D;

import java.util.ArrayList;

public abstract class EntityGroup implements IListener, IEntityGroup {

    final GameCanvas gc;

    // STATIC
    private static int HIGHEST_ID = -1;

    // CLASS VARIABLES
    public final int ID;

    private final ArrayList<Entity> entities = new ArrayList<>();
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
        for (Entity entity : entities){
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.softKill();
            }
        }
        entities.clear();
    }

    @Override
    public void killGroup(){
        killAllEntities();
        gc.eventGroupDead.trigger(new ComponentGroupDead(this));
    }

    @Override
    public boolean contains(LivingEntity entity){
        return entities.contains(entity);
    }

    public void makeEntitiesMove(Vector2D targetPosition, boolean canTeleporte){
        for (Entity entity : entities){

            Vector2D position = entity.getWorldPosition();
            Vector2D targetVector = targetPosition.sub(position);
            if (canTeleporte && targetVector.getLength() > gc.screenWidth){
                entity.setWorldPosition(targetPosition);
            }
            else {
                Vector2D targetDirection = targetVector.getNormalized();
                entity.setMoveDirectionVector(targetDirection);
                entity.move(gc.dt);
            }

//            Vector2D repulsion = Vector2D.ZERO;
//            for (LivingEntity other : entities){
//
//                Vector2D diff = entity.getWorldPosition().sub(other.getWorldPosition());
//                if (diff.getLength() < gc.tileSize) {
//                    if (diff.equals(Vector2D.ZERO)) {
//                        diff = Vector2D.getRandomVectorNormalized();
//                    }
//                    diff.normalize();
//                    repulsion = repulsion.add(diff);
//                }
//            }
//            entity.move(repulsion);
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity deadEntity, LivingEntity killer)){
            removeEntity(deadEntity);
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}

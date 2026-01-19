package com.example.app.entity.group;

import com.example.app.GameCanvas;
import com.example.app.entity.LivingEntity;
import com.example.app.entity.animals.*;
import com.example.app.event.*;
import com.example.app.event.component.ComponentEntityDead;
import com.example.app.event.component.ComponentGroupDead;
import com.example.app.event.component.IEventComponent;
import com.example.app.ui.IUpdatable;
import com.example.app.utils.Vector2D;

import java.util.ArrayList;

public abstract class EntityGroup implements IListener, IEntityGroup, IUpdatable {

    final GameCanvas gc;

    // STATIC
    private static int NEXT_ID = 0;

    // CLASS VARIABLES
    public final int id;
    private LivingEntity master = null;

    public final ArrayList<LivingEntity> entities = new ArrayList<>();
    private final ArrayList<LivingEntity> entitiesToRemove = new ArrayList<>();
    private int removeCount = 0;
    private int entityCount = 0;

    // ANIMAL TYPE
    public int animalType = -1;

    public EntityGroup(GameCanvas gc){
        id = NEXT_ID++;

        this.gc = gc;

        register(gc.eventEntityDead);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public LivingEntity getMaster() {
        return master;
    }

    public void setMaster(LivingEntity entity){
        entity.setOwnBehavior(true);
        master = entity;
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
        if (entity instanceof Mouse){
            return 0;
        } else if (entity instanceof Rat) {
            return 1;
        } else if (entity instanceof Rabbit) {
            return 2;
        } else if (entity instanceof Cat) {
            return 3;
        } else if (entity instanceof Dog) {
            return 4;
        } else if (entity instanceof Fox) {
            return 5;
        } else if (entity instanceof Wolf) {
            return 6;
        } else if (entity instanceof Bear) {
            return 7;
        }else {
            return -1;
        }
    }

    @Override
    public void addEntity(LivingEntity entity){
        if (animalType == -1){
            animalType = entityToInt(entity);
        }

        entity.setGroupID(id);
        entity.setOwnBehavior(false);

        gc.entityM.safeAddEntity(entity);

        entities.add(entity);
        entityCount++;

        if (master == null){
            setMaster(entity);
        }
    }

    public boolean removeEntity(LivingEntity entity){
        if (entities.remove(entity)){
            entityCount--;
            if (entity.equals(master) && entityCount > 0){
                master = entities.getFirst();
            }

            if (entityCount == 0) {
                killGroup();
            }
            return true;
        }
        return false;
    }

    @Override
    public void safeRemoveEntity(LivingEntity entity){
        entitiesToRemove.add(entity);
        removeCount = 2;
    }

    @Override
    public void killGroup(){
        gc.eventGroupDead.trigger(new ComponentGroupDead(this));
    }

    @Override
    public boolean contains(LivingEntity entity){
        return entities.contains(entity);
    }

    public LivingEntity getWeekest(){
        LivingEntity weekest = entities.getFirst();

        for (LivingEntity entity : entities){
            if (entity.getHealth() < weekest.getHealth()){
                weekest = entity;
            }
        }

        return weekest;
    }

    @Override
    public int size(){
        return entities.size();
    }

    @Override
    public boolean isEmpty(){
        return size() == 0;
    }

    public void makeEntitiesMove(Vector2D targetPosition, boolean canTeleporte){
        makeEntitiesMove(targetPosition, canTeleporte, false);
    }

    public void makeEntitiesMove(Vector2D targetPosition, boolean canTeleporte, boolean notTooClose){
        for (LivingEntity entity : entities){

            if (!entity.equals(master)){
                Vector2D position = entity.getWorldPosition();
                Vector2D targetVector = targetPosition.sub(position);
                if (notTooClose && targetVector.getLength() < gc.tileSize){
                    continue;
                }

                if (canTeleporte && targetVector.getLength() > gc.screenWidth){
                    entity.setWorldPosition(targetPosition);
                }
                else {
                    Vector2D targetDirection = targetVector.getNormalized();
                    entity.setMoveDirectionVector(targetDirection);
                    entity.move(new ArrayList<>(entities), gc.dt);
                }
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
    public void update() {

        removeCount--;
        if (removeCount <= 0){
            for (LivingEntity entity : entitiesToRemove){
                removeEntity(entity);
            }
            entitiesToRemove.clear();
        }
    }

    @Override
    public void onTrigger(IEventComponent component) {
        if (component instanceof ComponentEntityDead(LivingEntity deadEntity, LivingEntity killer)){
            safeRemoveEntity(deadEntity);
        }
    }

    @Override
    public void register(IEvent event) {
        event.addListener(this);
    }
}
